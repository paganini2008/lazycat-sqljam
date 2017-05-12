package lazycat.series.sqljam.feature;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lazycat.series.collection.ListUtils;
import lazycat.series.collection.RandomStringUtils;
import lazycat.series.lang.ArrayUtils;
import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.JdbcAdmin;
import lazycat.series.sqljam.JdbcException;
import lazycat.series.sqljam.JdbcUtils;
import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.ForeignKeyDefinition;
import lazycat.series.sqljam.relational.PrimaryKeyDefinition;
import lazycat.series.sqljam.relational.TableDefinition;
import lazycat.series.sqljam.relational.UniqueKeyDefinition;

/**
 * BasicFeature
 * 
 * @author Fred Feng
 * @version 1.0
 */
public abstract class BasicFeature extends Feature {

	static final int MODIFY_AUTO_INCREMENT = 1;
	static final int MODIFY_DATA_TYPE = 2;
	static final int MODIFY_NULLABLE = 3;
	static final int MODIFY_DEFAULT = 4;
	static final int MODIFY_COMMENT = 5;

	private final Map<String, String> metadataColumns = new HashMap<String, String>();

	protected BasicFeature() {
		mapMetadataColumn("TABLE_NAME", "TABLE_NAME");
		mapMetadataColumn("COLUMN_NAME", "COLUMN_NAME");
		mapMetadataColumn("PK_NAME", "PK_NAME");
		mapMetadataColumn("FK_NAME", "FK_NAME");
		mapMetadataColumn("INDEX_NAME", "INDEX_NAME");
	}

	protected final void mapMetadataColumn(String tag, String column) {
		metadataColumns.put(tag, column);
	}

	protected abstract String getAddColumnSqlString();

	protected abstract String getDropColumnSqlString();

	protected abstract String getModifyColumnSqlString();

	protected abstract String getAddPrimaryKeySqlString();

	protected abstract String getSetPrimaryKeySqlString();

	protected abstract String getDropPrimaryKeySqlString();

	protected abstract String getAddForeignKeySqlString();

	protected abstract String getDropForeignKeySqlString();

	protected abstract String getSetForeignKeySqlString();

	protected abstract String getAddUniqueKeySqlString();

	protected abstract String getDropUniqueKeySqlString();

	protected abstract String getSetUniqueKeySqlString();

	protected abstract String getIdentitySqlString();

	protected String getSetNullableSqlString() {
		throw new UnsupportedOperationException();
	}

	protected String getSetDefaultValueSqlString() {
		throw new UnsupportedOperationException();
	}

	protected String getCreateSequenceSqlString() {
		return "create sequence ?";
	}

	protected boolean isSequenceSupported() {
		return false;
	}

	protected String getDefaultPrimaryKeyName(String tableName, String[] columnNames) {
		return "pk_" + tableName + "_id";
	}

	protected String getDefaultForeignKeyName(String tableName, String[] columnNames, String refTableName, String[] refColumnNames) {
		return "fk_" + refTableName + "_" + RandomStringUtils.randomString(4, true, true, false);
	}

	protected String getDefaultUniqueKeyName(String tableName, String[] columnNames) {
		return "uk_" + RandomStringUtils.randomString(4, true, true, false);
	}

	protected String getDefaultSequenceName(String tableName, String columnName) {
		return "seq_" + tableName + "_" + columnName;
	}

	protected boolean isForeignKeyModified(ForeignKeyDefinition fkDefinition, JdbcAdmin jdbcAdmin, DatabaseMetaData dbmd) {
		ColumnDefinition columnDefinition = fkDefinition.getColumnDefinition();
		TableDefinition tableDefinition = columnDefinition.getTableDefinition();
		TableDefinition refTableDefinition = jdbcAdmin.getConfiguration().getTableDefinition(fkDefinition.getRefMappedClass());
		ColumnDefinition refColumnDefinition = jdbcAdmin.getConfiguration().getColumnDefinition(fkDefinition.getRefMappedClass(),
				fkDefinition.getRefMappedProperty());
		Iterator<Map<String, Object>> iter;
		try {
			iter = JdbcUtils.foreignKeyMetadataIterator(dbmd, null, refTableDefinition.getSchema(), refTableDefinition.getTableName());
		} catch (SQLException e) {
			throw new JdbcException(e);
		}
		Map<String, Object> metadata;
		for (; iter.hasNext();) {
			metadata = iter.next();
			if (tableDefinition.getTableName().equalsIgnoreCase(getMetadataValue(metadata, "FKTABLE_NAME"))
					&& columnDefinition.getColumnName().equalsIgnoreCase(getMetadataValue(metadata, "FKCOLUMN_NAME"))
					&& refTableDefinition.getTableName().equalsIgnoreCase(getMetadataValue(metadata, "PKTABLE_NAME"))
					&& refColumnDefinition.getColumnName().equalsIgnoreCase(getMetadataValue(metadata, "PKCOLUMN_NAME"))
					&& (StringUtils.isBlank(fkDefinition.getConstraintName())
							|| fkDefinition.getConstraintName().equalsIgnoreCase(getMetadataValue(metadata, "FK_NAME")))) {
				return false;
			}
		}
		return true;
	}

	protected boolean isPrimaryKeyModified(PrimaryKeyDefinition pkDefinition, DatabaseMetaData dbmd) {
		ColumnDefinition columnDefinition = pkDefinition.getColumnDefinition();
		TableDefinition tableDefinition = columnDefinition.getTableDefinition();
		Iterator<Map<String, Object>> iter;
		try {
			iter = JdbcUtils.primaryKeyMetadataIterator(dbmd, null, tableDefinition.getSchema(), tableDefinition.getTableName());
		} catch (SQLException e) {
			throw new JdbcException(e);
		}
		Map<String, Object> metadata;
		for (; iter.hasNext();) {
			metadata = iter.next();
			if (tableDefinition.getTableName().equalsIgnoreCase(getMetadataValue(metadata, "TABLE_NAME"))
					&& columnDefinition.getColumnName().equalsIgnoreCase(getMetadataValue(metadata, "COLUMN_NAME"))
					&& (StringUtils.isBlank(pkDefinition.getConstraintName())
							|| pkDefinition.getConstraintName().equalsIgnoreCase(getMetadataValue(metadata, "PK_NAME")))) {
				return false;
			}
		}
		return true;
	}

	protected boolean isUniqueKeyModified(UniqueKeyDefinition uqDefinition, DatabaseMetaData dbmd) {
		ColumnDefinition columnDefinition = uqDefinition.getColumnDefinition();
		TableDefinition tableDefinition = columnDefinition.getTableDefinition();
		Iterator<Map<String, Object>> iter;
		try {
			iter = JdbcUtils.uniqueKeyMetadataIterator(dbmd, null, tableDefinition.getSchema(), tableDefinition.getTableName());
		} catch (SQLException e) {
			throw new JdbcException(e);
		}
		Map<String, Object> metadata;
		for (; iter.hasNext();) {
			metadata = iter.next();
			if (tableDefinition.getTableName().equalsIgnoreCase(getMetadataValue(metadata, "TABLE_NAME"))
					&& columnDefinition.getColumnName().equalsIgnoreCase(getMetadataValue(metadata, "COLUMN_NAME"))
					&& (StringUtils.isBlank(uqDefinition.getConstraintName())
							|| uqDefinition.getConstraintName().equalsIgnoreCase(getMetadataValue(metadata, "INDEX_NAME")))) {
				return false;
			}
		}
		return true;
	}

	protected final String getMetadataValue(Map<String, Object> metadata, String key) {
		Object result = metadataColumns.containsKey(key) ? metadata.get(metadataColumns.get(key)) : metadata.get(key);
		return result != null ? result.toString() : "";
	}

	protected List<String> defineAddUniqueKeys(TableDefinition tableDefinition, JdbcAdmin jdbcAdmin) {
		List<String> sqls = new ArrayList<String>();
		UniqueKeyDefinition[] ukDefinitions = tableDefinition.getUniqueKeyDefinitions();
		if (ukDefinitions != null) {
			for (Map.Entry<String, List<UniqueKeyDefinition>> entry : transferUniqueKeys(ukDefinitions).entrySet()) {
				String sql = defineAddUniqueKey(tableDefinition, entry.getKey(), entry.getValue(), jdbcAdmin);
				sqls.add(sql);
			}
		}
		return sqls;
	}

	protected List<String> defineUniqueKeys(TableDefinition tableDefinition, JdbcAdmin jdbcAdmin) {
		List<String> sqls = new ArrayList<String>();
		UniqueKeyDefinition[] ukDefinitions = tableDefinition.getUniqueKeyDefinitions();
		if (ukDefinitions != null) {
			if (ukDefinitions != null) {
				for (Map.Entry<String, List<UniqueKeyDefinition>> entry : transferUniqueKeys(ukDefinitions).entrySet()) {
					String sql = defineUniqueKey(tableDefinition, entry.getKey(), entry.getValue(), jdbcAdmin);
					sqls.add(sql);
				}
			}
		}
		return sqls;
	}

	protected List<String> defineAddForeignKeys(final TableDefinition tableDefinition, final JdbcAdmin jdbcAdmin) {
		List<String> sqls = new ArrayList<String>();
		ForeignKeyDefinition[] fkDefinitions = tableDefinition.getForeignKeyDefinitions();
		if (fkDefinitions != null) {
			for (Map.Entry<String, List<ForeignKeyDefinition>> entry : transferForeignKeys(fkDefinitions).entrySet()) {
				String sql = defineAddForeignKey(tableDefinition, entry.getKey(), entry.getValue(), jdbcAdmin);
				sqls.add(sql);
			}
		}
		return sqls;
	}

	protected List<String> defineForeignKeys(final TableDefinition tableDefinition, final JdbcAdmin jdbcAdmin) {
		List<String> sqls = new ArrayList<String>();
		ForeignKeyDefinition[] fkDefinitions = tableDefinition.getForeignKeyDefinitions();
		if (fkDefinitions != null) {
			for (Map.Entry<String, List<ForeignKeyDefinition>> entry : transferForeignKeys(fkDefinitions).entrySet()) {
				String sql = defineForeignKey(tableDefinition, entry.getKey(), entry.getValue(), jdbcAdmin);
				sqls.add(sql);
			}
		}
		return sqls;
	}

	protected String defineDropForeignKey(TableDefinition tableDefinition, String constaintName, List<ForeignKeyDefinition> fkDefinitions,
			JdbcAdmin jdbcAdmin) {
		String[] args = { tableDefinition.getTableName(), matchForeignKeyName(tableDefinition, constaintName, fkDefinitions, jdbcAdmin) };
		return formatString(getDropForeignKeySqlString(), args);
	}

	private String matchForeignKeyName(TableDefinition tableDefinition, String constaintName, List<ForeignKeyDefinition> fkDefinitions,
			JdbcAdmin jdbcAdmin) {
		ForeignKeyDefinition firstDefinition = ListUtils.getFirst(fkDefinitions);
		TableDefinition refTableDefinition = jdbcAdmin.getConfiguration().getTableDefinition(firstDefinition.getRefMappedClass());
		if (StringUtils.isNotBlank(constaintName)) {
			Connection connection = jdbcAdmin.getConnectionProvider().openConnectionImplicitly();
			boolean constaintExists;
			try {
				constaintExists = JdbcUtils.foreignKeyExists(connection.getMetaData(), null, refTableDefinition.getSchema(),
						refTableDefinition.getTableName(), constaintName);
			} catch (SQLException e) {
				throw new JdbcException(e);
			} finally {
				jdbcAdmin.getConnectionProvider().closeConnection(connection);
			}
			if (constaintExists) {
				return constaintName;
			}
		}

		Connection connection = jdbcAdmin.getConnectionProvider().openConnectionImplicitly();
		Iterator<Map<String, Object>> iterator;
		try {
			iterator = JdbcUtils.foreignKeyMetadataIterator(connection.getMetaData(), null, refTableDefinition.getSchema(),
					refTableDefinition.getTableName());
		} catch (SQLException e) {
			throw new JdbcException(e);
		} finally {
			jdbcAdmin.getConnectionProvider().closeConnection(connection);
		}
		List<Map<String, Object>> metadataList = new ArrayList<Map<String, Object>>();
		for (; iterator.hasNext();) {
			Map<String, Object> metadata = iterator.next();
			for (ForeignKeyDefinition fkDefinition : fkDefinitions) {
				if (tableDefinition.getTableName().equals(metadata.get("FKTABLE_NAME"))
						&& fkDefinition.getColumnDefinition().getColumnName().equals(metadata.get("FKCOLUMN_NAME"))
						&& refTableDefinition.getTableName().equals(metadata.get("PKTABLE_NAME"))
						&& jdbcAdmin.getConfiguration()
								.getColumnDefinition(fkDefinition.getRefMappedClass(), fkDefinition.getRefMappedProperty()).getColumnName()
								.equals(metadata.get("PKCOLUMN_NAME"))) {
					metadataList.add(metadata);
				}
			}
		}
		if (metadataList.size() > 0) {
			return getMetadataValue(metadataList.get(0), "FK_NAME");
		}
		throw new IllegalStateException();
	}

	private String matchPrimaryKeyName(TableDefinition tableDefinition, String constaintName, List<PrimaryKeyDefinition> pkDefinitions,
			JdbcAdmin jdbcAdmin) {
		if (StringUtils.isNotBlank(constaintName)) {
			Connection connection = jdbcAdmin.getConnectionProvider().openConnectionImplicitly();
			boolean constaintExists;
			try {
				constaintExists = JdbcUtils.primaryKeyExists(connection.getMetaData(), null, tableDefinition.getSchema(),
						tableDefinition.getTableName(), constaintName);
			} catch (SQLException e) {
				throw new JdbcException(e);
			} finally {
				jdbcAdmin.getConnectionProvider().closeConnection(connection);
			}
			if (constaintExists) {
				return constaintName;
			}
		}

		Connection connection = jdbcAdmin.getConnectionProvider().openConnectionImplicitly();
		Iterator<Map<String, Object>> iterator;
		try {
			iterator = JdbcUtils.primaryKeyMetadataIterator(connection.getMetaData(), null, tableDefinition.getSchema(),
					tableDefinition.getTableName());
		} catch (SQLException e) {
			throw new JdbcException(e);
		} finally {
			jdbcAdmin.getConnectionProvider().closeConnection(connection);
		}
		List<Map<String, Object>> metadataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> metadata;
		for (; iterator.hasNext();) {
			metadata = iterator.next();
			for (PrimaryKeyDefinition pkDefinition : pkDefinitions) {
				if (tableDefinition.getTableName().equalsIgnoreCase(getMetadataValue(metadata, "TABLE_NAME"))
						&& pkDefinition.getColumnDefinition().getColumnName().equalsIgnoreCase(getMetadataValue(metadata, "COLUMN_NAME"))) {
					metadataList.add(metadata);
				}
			}
		}
		if (metadataList.size() > 0) {
			return getMetadataValue(metadataList.get(0), "PK_NAME");
		}
		throw new IllegalStateException();
	}

	private String matchUniqueKeyName(TableDefinition tableDefinition, String constaintName, List<UniqueKeyDefinition> uqDefinitions,
			JdbcAdmin jdbcAdmin) {
		if (StringUtils.isNotBlank(constaintName)) {
			Connection connection = jdbcAdmin.getConnectionProvider().openConnectionImplicitly();
			boolean constaintExists;
			try {
				constaintExists = JdbcUtils.foreignKeyExists(connection.getMetaData(), null, tableDefinition.getSchema(),
						tableDefinition.getTableName(), constaintName);
			} catch (SQLException e) {
				throw new JdbcException(e);
			} finally {
				jdbcAdmin.getConnectionProvider().closeConnection(connection);
			}
			if (constaintExists) {
				return constaintName;
			}
		}

		Connection connection = jdbcAdmin.getConnectionProvider().openConnectionImplicitly();
		Iterator<Map<String, Object>> iterator;
		try {
			iterator = JdbcUtils.uniqueKeyMetadataIterator(connection.getMetaData(), null, tableDefinition.getSchema(),
					tableDefinition.getTableName());
		} catch (SQLException e) {
			throw new JdbcException(e);
		} finally {
			jdbcAdmin.getConnectionProvider().closeConnection(connection);
		}
		List<Map<String, Object>> metadataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> metadata;
		for (; iterator.hasNext();) {
			metadata = iterator.next();
			for (UniqueKeyDefinition uqDefinition : uqDefinitions) {
				if (tableDefinition.getTableName().equalsIgnoreCase(getMetadataValue(metadata, "TABLE_NAME"))
						&& uqDefinition.getColumnDefinition().getColumnName().equalsIgnoreCase(getMetadataValue(metadata, "COLUMN_NAME"))) {
					metadataList.add(metadata);
				}
			}
		}
		if (metadataList.size() > 0) {
			return getMetadataValue(metadataList.get(0), "INDEX_NAME");
		}
		return "";
	}

	protected List<String> defineAddPrimaryKey(TableDefinition tableDefinition) {
		List<String> sqls = new ArrayList<String>();
		PrimaryKeyDefinition[] pkDefinitions = tableDefinition.getPrimaryKeyDefinitions();
		if (pkDefinitions != null) {
			List<String> columnNames = new ArrayList<String>();
			String constaintName = null;
			for (PrimaryKeyDefinition pkDefinition : pkDefinitions) {
				columnNames.add(pkDefinition.getColumnDefinition().getColumnName());
				if (StringUtils.isBlank(constaintName)) {
					constaintName = pkDefinition.getConstraintName();
				}
			}
			if (StringUtils.isBlank(constaintName)) {
				constaintName = getDefaultPrimaryKeyName(tableDefinition.getTableName(), columnNames.toArray(new String[0]));
			}
			String[] args = { tableDefinition.getTableName(), constaintName, ArrayUtils.join(columnNames.toArray(), ",") };
			sqls.add(formatString(getAddPrimaryKeySqlString(), args));
		}
		return sqls;
	}

	protected String definePrimaryKey(TableDefinition tableDefinition) {
		PrimaryKeyDefinition[] pkDefinitions = tableDefinition.getPrimaryKeyDefinitions();
		if (pkDefinitions != null) {
			List<String> columnNames = new ArrayList<String>();
			String constaintName = null;
			for (PrimaryKeyDefinition pkDefinition : pkDefinitions) {
				columnNames.add(pkDefinition.getColumnDefinition().getColumnName());
				if (StringUtils.isBlank(constaintName)) {
					constaintName = pkDefinition.getConstraintName();
				}
			}
			if (StringUtils.isBlank(constaintName)) {
				constaintName = getDefaultPrimaryKeyName(tableDefinition.getTableName(), columnNames.toArray(new String[0]));
			}
			String[] args = { constaintName, ArrayUtils.join(columnNames.toArray(), ",") };
			return formatString(getSetPrimaryKeySqlString(), args);
		}
		return "";
	}

	protected List<String> defineAddColumn(ColumnDefinition columnDefinition) {
		List<String> sqls = new ArrayList<String>();
		TableDefinition tableDefinition = columnDefinition.getTableDefinition();
		String[] args = { tableDefinition.getTableName(), defineColumn(columnDefinition) };
		sqls.add(formatString(getAddColumnSqlString(), args));
		return sqls;
	}

	protected List<String> defineModifyColumn(ColumnDefinition columnDefinition, int[] effects) {
		List<String> sqls = new ArrayList<String>();
		TableDefinition tableDefinition = columnDefinition.getTableDefinition();
		String[] args = { tableDefinition.getTableName(), defineColumn(columnDefinition) };
		sqls.add(formatString(getModifyColumnSqlString(), args));
		return sqls;
	}

	protected List<String> defineDropColumn(ColumnDefinition columnDefinition) {
		List<String> sqls = new ArrayList<String>();
		TableDefinition tableDefinition = columnDefinition.getTableDefinition();
		String[] args = { tableDefinition.getTableName(), columnDefinition.getColumnName() };
		sqls.add(formatString(getDropColumnSqlString(), args));
		return sqls;
	}

	protected String defineAddUniqueKey(TableDefinition tableDefinition, String constaintName, List<UniqueKeyDefinition> uqDefinitions,
			JdbcAdmin jdbcAdmin) {
		List<String> columnNames = new ArrayList<String>();
		for (UniqueKeyDefinition ukDefinition : uqDefinitions) {
			columnNames.add(ukDefinition.getColumnDefinition().getColumnName());
		}
		if (StringUtils.isBlank(constaintName)) {
			constaintName = getDefaultUniqueKeyName(tableDefinition.getTableName(), columnNames.toArray(new String[0]));
		}
		String[] args = { tableDefinition.getTableName(), constaintName, ArrayUtils.join(columnNames.toArray(), ",") };
		return formatString(getAddUniqueKeySqlString(), args);
	}

	protected String defineUniqueKey(TableDefinition tableDefinition, String constaintName, List<UniqueKeyDefinition> uqDefinitions,
			JdbcAdmin jdbcAdmin) {
		List<String> columnNames = new ArrayList<String>();
		for (UniqueKeyDefinition ukDefinition : uqDefinitions) {
			columnNames.add(ukDefinition.getColumnDefinition().getColumnName());
		}
		if (StringUtils.isBlank(constaintName)) {
			constaintName = getDefaultUniqueKeyName(tableDefinition.getTableName(), columnNames.toArray(new String[0]));
		}
		String[] args = { constaintName, ArrayUtils.join(columnNames.toArray(), ",") };
		return formatString(getSetUniqueKeySqlString(), args);
	}

	protected String defineDropUniqueKey(TableDefinition tableDefinition, String constaintName, List<UniqueKeyDefinition> uqDefinitions,
			JdbcAdmin jdbcAdmin) {
		constaintName = matchUniqueKeyName(tableDefinition, constaintName, uqDefinitions, jdbcAdmin);
		String[] args = { tableDefinition.getTableName(), constaintName };
		return formatString(getDropUniqueKeySqlString(), args);
	}

	protected String defineAddForeignKey(TableDefinition tableDefinition, String constaintName, List<ForeignKeyDefinition> fkDefinitions,
			JdbcAdmin jdbcAdmin) {
		List<String> columnNames = new ArrayList<String>();
		List<String> refColumnNames = new ArrayList<String>();
		for (ForeignKeyDefinition fkDefinition : fkDefinitions) {
			columnNames.add(fkDefinition.getColumnDefinition().getColumnName());
			refColumnNames.add(jdbcAdmin.getConfiguration()
					.getColumnDefinition(fkDefinition.getRefMappedClass(), fkDefinition.getRefMappedProperty()).getColumnName());
		}
		ForeignKeyDefinition fkDefinition = ListUtils.getFirst(fkDefinitions);
		TableDefinition reftTableDefinition = jdbcAdmin.getConfiguration().getTableDefinition(fkDefinition.getRefMappedClass());
		if (StringUtils.isBlank(constaintName)) {
			constaintName = getDefaultForeignKeyName(tableDefinition.getTableName(), columnNames.toArray(new String[0]),
					reftTableDefinition.getTableName(), refColumnNames.toArray(new String[0]));
		}
		String[] args = { tableDefinition.getTableName(), constaintName, ArrayUtils.join(columnNames.toArray(), ","),
				reftTableDefinition.getTableName(), ArrayUtils.join(refColumnNames.toArray(), ","), onUpdate(fkDefinition.getOnUpdate()),
				onDelete(fkDefinition.getOnDelete()) };
		return formatString(getAddForeignKeySqlString(), args);
	}

	protected String defineForeignKey(TableDefinition tableDefinition, String constaintName, List<ForeignKeyDefinition> fkDefinitions,
			JdbcAdmin jdbcAdmin) {
		List<String> columnNames = new ArrayList<String>();
		List<String> refColumnNames = new ArrayList<String>();
		for (ForeignKeyDefinition fkDefinition : fkDefinitions) {
			columnNames.add(fkDefinition.getColumnDefinition().getColumnName());
			refColumnNames.add(jdbcAdmin.getConfiguration()
					.getColumnDefinition(fkDefinition.getRefMappedClass(), fkDefinition.getRefMappedProperty()).getColumnName());
		}
		ForeignKeyDefinition fkDefinition = ListUtils.getFirst(fkDefinitions);
		TableDefinition reftTableDefinition = jdbcAdmin.getConfiguration().getTableDefinition(fkDefinition.getRefMappedClass());
		if (StringUtils.isBlank(constaintName)) {
			constaintName = getDefaultForeignKeyName(tableDefinition.getTableName(), columnNames.toArray(new String[0]),
					reftTableDefinition.getTableName(), refColumnNames.toArray(new String[0]));
		}
		String[] args = { constaintName, ArrayUtils.join(columnNames.toArray(), ","), reftTableDefinition.getTableName(),
				ArrayUtils.join(refColumnNames.toArray(), ","), onUpdate(fkDefinition.getOnUpdate()),
				onDelete(fkDefinition.getOnDelete()) };
		return formatString(getSetForeignKeySqlString(), args);
	}

	protected String defineDropPrimaryKey(TableDefinition tableDefinition, String constaintName, List<PrimaryKeyDefinition> pkDefinitions,
			JdbcAdmin jdbcAdmin) {
		constaintName = matchPrimaryKeyName(tableDefinition, constaintName, pkDefinitions, jdbcAdmin);
		String[] args = { tableDefinition.getTableName(), constaintName };
		return formatString(getDropPrimaryKeySqlString(), args);
	}

	public String getPageableSqlString(String sql, int offset, int limit) {
		StringBuilder text = new StringBuilder();
		text.append(sql);
		text.append(" limit ").append(limit);
		text.append(" offset ").append(offset);
		return text.toString();
	}

}
