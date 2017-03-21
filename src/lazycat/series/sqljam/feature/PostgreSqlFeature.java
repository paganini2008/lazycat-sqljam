package lazycat.series.sqljam.feature;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.lang.Booleans;
import lazycat.series.lang.Ints;
import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.JdbcFault;
import lazycat.series.sqljam.JdbcUtils;
import lazycat.series.sqljam.MappingException;
import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * PostgreSqlFeature
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class PostgreSqlFeature extends BasicFeature {

	public PostgreSqlFeature() {
		registerColumnType(JdbcType.BIT, "bit($l)");
		registerColumnType(JdbcType.BOOLEAN, "bool");
		registerColumnType(JdbcType.BIGINT, "int8");
		registerColumnType(JdbcType.SMALLINT, "int2");
		registerColumnType(JdbcType.TINYINT, "int2");
		registerColumnType(JdbcType.INTEGER, "int4");
		registerColumnType(JdbcType.CHAR, "char($l)");
		registerColumnType(JdbcType.VARCHAR, "varchar($l)");
		registerColumnType(JdbcType.VARCHAR, 255, "varchar($l)");
		registerColumnType(JdbcType.REAL, "float4");
		registerColumnType(JdbcType.FLOAT, "float4");
		registerColumnType(JdbcType.DOUBLE, "float8");
		registerColumnType(JdbcType.DATE, "date");
		registerColumnType(JdbcType.TIME, "time");
		registerColumnType(JdbcType.TIMESTAMP, "timestamp");
		registerColumnType(JdbcType.VARBINARY, "bytea");
		registerColumnType(JdbcType.BINARY, "bytea");
		registerColumnType(JdbcType.LONGVARCHAR, "text");
		registerColumnType(JdbcType.LONGVARBINARY, "bytea");
		registerColumnType(JdbcType.CLOB, "text");
		registerColumnType(JdbcType.BLOB, "oid");
		registerColumnType(JdbcType.NUMERIC, "numeric($p,$s)");
		registerColumnType(JdbcType.DECIMAL, "numeric($p,$s)");

		registerJavaType(Boolean.TYPE, JdbcType.BOOLEAN);
		registerJavaType(Boolean.class, JdbcType.BOOLEAN);
	}

	public static void main(String[] args) {
		PostgreSqlFeature feature = new PostgreSqlFeature();
		System.out.println(feature.getColumnType(JdbcType.VARCHAR, -1));
	}

	protected String getAddColumnSqlString() {
		return "alter table ? add ?";
	}

	protected String getDropColumnSqlString() {
		return "alter table ? drop ?";
	}

	protected String getModifyColumnSqlString() {
		return "alter table ? alter ? type ?";
	}

	protected String getAddPrimaryKeySqlString() {
		return "alter table add constraint ? primary key (?)";
	}

	protected String getSetPrimaryKeySqlString() {
		return "constraint ? primary key (?)";
	}

	protected String getDropPrimaryKeySqlString() {
		return "alter table ? drop constraint ?";
	}

	protected String getAddForeignKeySqlString() {
		return "alter table ? add constraint ? foreign key(?) references ?(?) ? ?";
	}

	protected String getDropForeignKeySqlString() {
		return "alter table ? drop constraint ?";
	}

	protected String getSetForeignKeySqlString() {
		return "constraint ? foreign key(?) references ?(?) ? ?";
	}

	protected String getAddUniqueKeySqlString() {
		return "alter table ? add constraint ? unique(?)";
	}

	protected String getDropUniqueKeySqlString() {
		return "alter table ? drop constraint ?";
	}

	protected String getSetUniqueKeySqlString() {
		return "constraint ? unique(?)";
	}

	protected String getAddTableCommentSqlString() {
		return "comment on table ? is '?'";
	}

	protected String getSetNullableSqlString() {
		return "alter table ? alter column ? set not null";
	}

	protected String getSetDefaultValueSqlString() {
		return "alter table ? alter column ? set default ?";
	}

	protected String getCreateSequenceSqlString() {
		return "create sequence if not exists ? increment by 1 minvalue 1 no maxvalue start with 1 owned by ?";
	}

	protected String getIdentitySqlString() {
		throw new UnsupportedOperationException();
	}

	public String selectUUID() {
		throw new UnsupportedOperationException();
	}

	protected String getIdentitySqlString(JdbcType jdbcType) {
		throw new UnsupportedOperationException();
	}

	protected String getAddTableColumnCommentSqlString() {
		return "comment on column ?.? is '?'";
	}

	protected int[] getColumnModified(ColumnDefinition columnDefinition, DatabaseMetaData dbmd) {
		if ("price".equals(columnDefinition.getColumnName())) {
			System.out.println("PostgreSqlFeature.getColumnModified()");
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
			result = columnDefinition.isNullable() == nullable;
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

		if (!columnDefinition.isAutoIncrement()) {
			String defaultValue = (String) metadata.get("COLUMN_DEF");
			result = !StringUtils.hasContent(columnDefinition.getDefaultValue(), defaultValue);
			if (!result) {
				effects.add(MODIFY_DEFAULT);
			}
		}

		String comment = (String) metadata.get("REMARKS");
		result = StringUtils.equalsIgnoreCaseStrictly(columnDefinition.getComment(), comment);
		if (!result) {
			effects.add(MODIFY_COMMENT);
		}

		String typeName = (String) metadata.get("TYPE_NAME");
		JdbcType jdbcType = JdbcType.find((Integer) metadata.get("DATA_TYPE"));
		if (StringUtils.isNotBlank(typeName) && jdbcType != null) {
			JdbcType jdbcTypeFind = (columnDefinition.getJdbcType() == JdbcType.OTHER
					|| columnDefinition.getJdbcType() == JdbcType.OBJECT) ? getJdbcType(columnDefinition.getJavaType())
							: columnDefinition.getJdbcType();
			String typeNameFind;
			if (JdbcUtils.isNumeric(jdbcTypeFind)) {
				typeNameFind = getColumnType(jdbcTypeFind, columnDefinition.getPrecision(), columnDefinition.getScale());
			} else if (JdbcUtils.isInteger(jdbcTypeFind)) {
				typeNameFind = getColumnType(jdbcTypeFind);
			} else {
				typeNameFind = getColumnType(jdbcTypeFind, columnDefinition.getLength());
				Integer length = (Integer) metadata.get("COLUMN_SIZE");
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

	protected String[] defineModifyColumn(ColumnDefinition columnDefinition, int[] effects) {
		final List<String> sqls = new ArrayList<String>();
		TableDefinition tableDefinition = columnDefinition.getTableDefinition();
		for (int code : effects) {
			switch (code) {
			case MODIFY_DATA_TYPE:
				JdbcType jdbcType = columnDefinition.getJdbcType();
				if (jdbcType == null) {
					throw new MappingException("Undefined jdbcType.");
				}
				if (columnDefinition.getJdbcType() == JdbcType.OTHER || columnDefinition.getJdbcType() == JdbcType.OBJECT) {
					jdbcType = getJdbcType(columnDefinition.getJavaType());
				}
				String typeRepr;
				if (JdbcUtils.isNumeric(jdbcType)) {
					typeRepr = getColumnType(jdbcType, columnDefinition.getPrecision(), columnDefinition.getScale());
				} else if (JdbcUtils.isInteger(jdbcType)) {
					typeRepr = getColumnType(jdbcType);
				} else {
					typeRepr = getColumnType(jdbcType, columnDefinition.getLength());
				}
				sqls.add(formatString(getModifyColumnSqlString(),
						new String[] { tableDefinition.getTableName(), columnDefinition.getColumnName(), typeRepr }));
				break;
			case MODIFY_AUTO_INCREMENT:
				if (columnDefinition.isAutoIncrement()) {
					String seqName = getDefaultSequenceName(tableDefinition.getTableName(), columnDefinition.getColumnName());
					sqls.add(formatString(getCreateSequenceSqlString(),
							new String[] { seqName, tableDefinition.getTableName() + "." + columnDefinition.getColumnName() }));
					sqls.add(formatString(getSetDefaultValueSqlString(), new String[] { tableDefinition.getTableName(),
							columnDefinition.getColumnName(), "nextval('" + seqName + "')" }));
				}
				break;
			case MODIFY_NULLABLE:
				if (!columnDefinition.isNullable()) {
					sqls.add(formatString(getSetNullableSqlString(),
							new String[] { tableDefinition.getTableName(), columnDefinition.getColumnName() }));
				}
				break;
			case MODIFY_DEFAULT:
				if (StringUtils.isNotBlank(columnDefinition.getDefaultValue())) {
					sqls.add(formatString(getSetDefaultValueSqlString(), new String[] { tableDefinition.getTableName(),
							columnDefinition.getColumnName(), columnDefinition.getDefaultValue() }));
				}
				break;
			case MODIFY_COMMENT:
				if (StringUtils.isNotBlank(columnDefinition.getComment())) {
					String[] args = { tableDefinition.getTableName(), columnDefinition.getColumnName(),
							columnDefinition.getComment() };
					sqls.add(formatString(getAddTableColumnCommentSqlString(), args));
				}
				break;
			}
		}
		return sqls.toArray(new String[0]);
	}

	protected void afterCreateTable(TableDefinition tableDefinition, List<String> ddls) {
		for (ColumnDefinition columnDefinition : tableDefinition.getColumnDefinitions()) {
			if (columnDefinition.isAutoIncrement()) {
				String seqName = getDefaultSequenceName(tableDefinition.getTableName(), columnDefinition.getColumnName());
				ddls.add(formatString(getCreateSequenceSqlString(),
						new String[] { seqName, tableDefinition.getTableName() + "." + columnDefinition.getColumnName() }));
				ddls.add(formatString(getSetDefaultValueSqlString(), new String[] { tableDefinition.getTableName(),
						columnDefinition.getColumnName(), "nextval('" + seqName + "')" }));
			}
			if (!columnDefinition.isNullable()) {
				ddls.add(formatString(getSetNullableSqlString(),
						new String[] { tableDefinition.getTableName(), columnDefinition.getColumnName() }));
			}
			if (StringUtils.isNotBlank(columnDefinition.getDefaultValue())) {
				ddls.add(formatString(getSetDefaultValueSqlString(), new String[] { tableDefinition.getTableName(),
						columnDefinition.getColumnName(), columnDefinition.getDefaultValue() }));
			}
			if (StringUtils.isNotBlank(columnDefinition.getComment())) {
				String[] args = { tableDefinition.getTableName(), columnDefinition.getColumnName(),
						columnDefinition.getComment() };
				ddls.add(formatString(getAddTableColumnCommentSqlString(), args));
			}
		}
	}

	protected String defineColumn(ColumnDefinition columnDefinition) {
		if (StringUtils.isNotBlank(columnDefinition.getColumnScript())) {
			return columnDefinition.getColumnScript();
		} else {
			StringBuilder str = new StringBuilder();
			str.append(columnDefinition.getColumnName());
			JdbcType jdbcType = columnDefinition.getJdbcType();
			if (jdbcType == null) {
				throw new MappingException("Undefined jdbcType.");
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
			return str.toString();
		}
	}

	public String columnAs(String left, String right) {
		return left + " as \"" + right + "\"";
	}

}
