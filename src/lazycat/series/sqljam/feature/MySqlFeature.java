package lazycat.series.sqljam.feature;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.lang.Booleans;
import lazycat.series.lang.Ints;
import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.JdbcFault;
import lazycat.series.sqljam.JdbcUtils;
import lazycat.series.sqljam.MappingFault;
import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * MySqlFeature
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class MySqlFeature extends BasicFeature {

	public MySqlFeature() {
		registerColumnType(JdbcType.BIT, "bit");
		registerColumnType(JdbcType.BIGINT, "bigint");
		registerColumnType(JdbcType.TINYINT, "tinyint");
		registerColumnType(JdbcType.SMALLINT, "smallint");
		registerColumnType(JdbcType.INTEGER, "integer");
		registerColumnType(JdbcType.CHAR, "char($l)");
		registerColumnType(JdbcType.FLOAT, "float($p,$s)");
		registerColumnType(JdbcType.REAL, "double($p,$s)");
		registerColumnType(JdbcType.DOUBLE, "double($p,$s)");
		registerColumnType(JdbcType.BOOLEAN, "bit");
		registerColumnType(JdbcType.DATE, "date");
		registerColumnType(JdbcType.TIME, "time");
		registerColumnType(JdbcType.TIMESTAMP, "timestamp");
		registerColumnType(JdbcType.VARBINARY, "longblob");
		registerColumnType(JdbcType.VARBINARY, 16777215, "mediumblob");
		registerColumnType(JdbcType.VARBINARY, 65535, "blob");
		registerColumnType(JdbcType.VARBINARY, 255, "tinyblob");
		registerColumnType(JdbcType.BINARY, "binary($l)");
		registerColumnType(JdbcType.LONGVARBINARY, "longblob");
		registerColumnType(JdbcType.LONGVARBINARY, 16777215, "mediumblob");
		registerColumnType(JdbcType.DECIMAL, "decimal($p,$s)");
		registerColumnType(JdbcType.NUMERIC, "numeric($p,$s)");
		registerColumnType(JdbcType.BLOB, "longblob");
		registerColumnType(JdbcType.CLOB, "longtext");
		registerColumnType(JdbcType.VARCHAR, "longtext");
		registerColumnType(JdbcType.VARCHAR, 16777215, "mediumtext");
		registerColumnType(JdbcType.VARCHAR, 65535, "text");
		registerColumnType(JdbcType.VARCHAR, 255, "varchar($l)");
		registerColumnType(JdbcType.LONGVARCHAR, "longtext");
		registerColumnType(JdbcType.ENUM, "char($l)");

		mapMetadataColumn("PK_NAME", "INDEX_NAME");

		registerFunction("left", "left(?,?)");
		registerFunction("right", "right(?,?)");
	}

	protected String getAddTableCommentSqlString() {
		return "alter table ? comment='?'";
	}

	protected String getAddColumnSqlString() {
		return "alter table ? add column ?";
	}

	protected String getDropColumnSqlString() {
		return "alter table ? drop column ?";
	}

	protected String getModifyColumnSqlString() {
		return "alter table ? modify ?";
	}

	protected String getDropPrimaryKeySqlString() {
		return "alter table ? drop primary key";
	}

	protected String getAddPrimaryKeySqlString() {
		return "alter table ? add constraint ? primary key (?)";
	}

	protected String getSetPrimaryKeySqlString() {
		return "constraint ? primary key (?)";
	}

	protected String getSetUniqueKeySqlString() {
		return "constraint ? unique(?)";
	}

	protected String getAddUniqueKeySqlString() {
		return "alter table ? add constraint ? unique(?)";
	}

	protected String getDropUniqueKeySqlString() {
		return "alter table ? drop index ?";
	}

	protected String getAddForeignKeySqlString() {
		return "alter table ? add constraint ? foreign key (?) references ?(?) ? ?";
	}

	protected String getSetForeignKeySqlString() {
		return "constraint ? foreign key (?) references ?(?)";
	}

	protected String getDropForeignKeySqlString() {
		return "alter table ? drop foreign key ?";
	}

	protected String getIdentitySqlString() {
		return "auto_increment";
	}

	public String selectUUID() {
		return "select uuid()";
	}

	protected String getSetNullableSqlString() {
		return "alter table ? alter column ? set not null";
	}

	protected String getSetDefaultValueSqlString() {
		return "alter table ? alter column ? set default ?";
	}

	protected String defineColumn(ColumnDefinition columnDefinition) {
		if (StringUtils.isNotBlank(columnDefinition.getColumnScript())) {
			return columnDefinition.getColumnScript();
		} else {
			StringBuilder str = new StringBuilder();
			str.append(columnDefinition.getColumnName());
			JdbcType jdbcType = columnDefinition.getJdbcType();
			if (jdbcType == null) {
				throw new MappingFault("Undefined jdbcType.");
			}
			if (columnDefinition.getJdbcType() == JdbcType.OTHER || columnDefinition.getJdbcType() == JdbcType.OBJECT) {
				jdbcType = getJdbcType(columnDefinition.getJavaType());
			}
			if (JdbcUtils.isNumeric(jdbcType)) {
				str.append(" ").append(getColumnType(jdbcType, columnDefinition.getPrecision(), columnDefinition.getScale()));
			} else if (JdbcUtils.isInteger(jdbcType)) {
				str.append(" ").append(getColumnType(jdbcType));
			} else {
				str.append(" ").append(getColumnType(jdbcType, columnDefinition.getLength()));
			}
			if (columnDefinition.isUnsigned()) {
				str.append(" unsigned");
			}
			if (!columnDefinition.isNullable()) {
				str.append(" not null");
			}
			if (columnDefinition.isAutoIncrement()) {
				str.append(" ").append(getIdentitySqlString());
			}
			if (StringUtils.isNotBlank(columnDefinition.getDefaultValue())) {
				str.append(" default ").append(columnDefinition.getDefaultValue());
			}
			if (StringUtils.isNotBlank(columnDefinition.getComment())) {
				str.append(" comment '").append(columnDefinition.getComment()).append("'");
			}
			return str.toString();
		}
	}

	protected int[] getColumnModified(ColumnDefinition columnDefinition, DatabaseMetaData dbmd) {
		if (columnDefinition.getColumnName().equals("price")) {
			System.out.println();
		}
		TableDefinition tableDefinition = columnDefinition.getTableDefinition();
		boolean result = true;
		Map<String, Object> metadata;
		try {
			metadata = JdbcUtils.getColumnMetadata(dbmd, tableDefinition.getCatalog(), tableDefinition.getSchema(),
					tableDefinition.getTableName(), columnDefinition.getColumnName());
		} catch (SQLException e) {
			throw new JdbcFault(e);
		}

		Set<Integer> effects = new HashSet<Integer>();
		String parameter = (String) metadata.get("IS_NULLABLE");
		if (StringUtils.isNotBlank(parameter)) {
			boolean nullable = Booleans.parse((parameter));
			result = StringUtils.isNotBlank(columnDefinition.getDefaultValue()) || columnDefinition.isNullable() == nullable;
			if (!result) {
				effects.add(MODIFY_NULLABLE);
			}
		}

		parameter = (String) metadata.get("IS_AUTOINCREMENT");
		if (StringUtils.isNotBlank(parameter)) {
			boolean autoIncrement = Booleans.parse(parameter);
			result = columnDefinition.isAutoIncrement() == autoIncrement;
			if (!result) {
				effects.add(MODIFY_AUTO_INCREMENT);
			}
		}

		String defaultValue = (String) metadata.get("COLUMN_DEF");
		result = !StringUtils.hasContent(columnDefinition.getDefaultValue(), defaultValue);
		if (!result) {
			effects.add(MODIFY_DEFAULT);
		}

		String comment = (String) metadata.get("REMARKS");
		result = StringUtils.equalsIgnoreCaseStrictly(columnDefinition.getComment(), comment);
		if (!result) {
			effects.add(MODIFY_COMMENT);
		}

		String typeName = (String) metadata.get("TYPE_NAME");
		JdbcType jdbcType = JdbcType.find((Integer) metadata.get("DATA_TYPE"));
		if (StringUtils.isNotBlank(typeName) && jdbcType != null) {
			JdbcType jdbcTypeFind = (columnDefinition.getJdbcType() == JdbcType.OTHER || columnDefinition.getJdbcType() == JdbcType.OBJECT)
					? getJdbcType(columnDefinition.getJavaType()) : columnDefinition.getJdbcType();
			String typeNameFind;
			if (JdbcUtils.isNumeric(jdbcTypeFind)) {
				typeNameFind = getColumnType(jdbcTypeFind, columnDefinition.getPrecision(), columnDefinition.getScale());
				Number precision = (Number) metadata.get("COLUMN_SIZE");
				Number scale = (Number) metadata.get("DECIMAL_DIGITS");
				if (precision != null && scale != null && columnDefinition.getPrecision() > 0 && columnDefinition.getScale() > 0) {
					if (columnDefinition.getPrecision() != precision.intValue() || columnDefinition.getScale() != scale.intValue()) {
						effects.add(MODIFY_DATA_TYPE);
					}
				}

			} else if (JdbcUtils.isInteger(jdbcTypeFind)) {
				typeNameFind = getColumnType(jdbcTypeFind);
			} else {
				typeNameFind = getColumnType(jdbcTypeFind, columnDefinition.getLength());
				Number length = (Number) metadata.get("COLUMN_SIZE");
				if (length != null && columnDefinition.getLength() > 0) {
					result = columnDefinition.getLength() <= length.longValue();
					if (!result) {
						effects.add(MODIFY_DATA_TYPE);
					}
				}
			}
			result = (typeName.toLowerCase().startsWith(typeNameFind.toLowerCase())
					|| typeNameFind.toLowerCase().startsWith(typeName.toLowerCase())
					|| (jdbcTypeCheckedStrictly() && jdbcType == jdbcTypeFind)
					|| (!jdbcTypeCheckedStrictly() && isAssigned(jdbcTypeFind, jdbcType)));
			if (!result) {
				effects.add(MODIFY_DATA_TYPE);
			}
		}
		return effects.size() > 0 ? Ints.toIntArray(effects) : null;
	}

	public String getPageableSqlString(String sql, int offset, int limit) {
		StringBuilder text = new StringBuilder();
		text.append(sql);
		text.append(" limit ").append(offset).append(",").append(limit);
		return text.toString();
	}

	public static void main(String[] args) {
		MySqlFeature feature = new MySqlFeature();
		System.out.println(feature.getColumnType(JdbcType.VARCHAR, 4000));
	}

}
