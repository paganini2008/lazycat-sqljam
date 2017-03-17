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
import lazycat.series.lang.ArrayUtils;
import lazycat.series.lang.RandomUtils;
import lazycat.series.lang.StringUtils;
import lazycat.series.reflect.ConstructorUtils;
import lazycat.series.reflect.ReflectiveInvocationException;
import lazycat.series.sqljam.JdbcAdmin;
import lazycat.series.sqljam.JdbcFault;
import lazycat.series.sqljam.JdbcUtils;
import lazycat.series.sqljam.SessionException;
import lazycat.series.sqljam.query.Query;
import lazycat.series.sqljam.query.QueryImpl;
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
		return "fk_" + refTableName + "_" + RandomUtils.randomString(4);
	}

	protected String getDefaultUniqueKeyName(String tableName, String[] columnNames) {
		return "uk_" + RandomUtils.randomString(4);
	}

	protected String getDefaultSequenceName(String tableName, String columnName) {
		return "seq_" + tableName + "_" + columnName;
	}

	protected boolean isForeignKeyModified(ForeignKeyDefinition fkDefinition, JdbcAdmin jdbcAdmin, DatabaseMetaData dbmd) {
		ColumnDefinition columnDefinition = fkDefinition.getColumnDefinition();
		TableDefinition tableDefinition = columnDefinition.getTableDefinition();
		TableDefinition refTableDefinition = jdbcAdmin.getConfiguration().getMetaData().getTable(fkDefinition.getRefMappedClass());
		ColumnDefinition refColumnDefinition = jdbcAdmin.getConfiguration().getMetaData().getColumn(fkDefinition.getRefMappedClass(),
				fkDefinition.getRefMappedProperty());
		Iterator<Map<String, Object>> iter;
		try {
			iter = JdbcUtils.foreignKeyMetadataIterator(dbmd, refTableDefinition.getCatalog(), refTableDefinition.getSchema(),
					refTableDefinition.getTableName());
		} catch (SQLException e) {
			throw new JdbcFault(e);
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
			iter = JdbcUtils.primaryKeyMetadataIterator(dbmd, tableDefinition.getCatalog(), tableDefinition.getSchema(),
					tableDefinition.getTableName());
		} catch (SQLException e) {
			throw new JdbcFault(e);
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
			iter = JdbcUtils.uniqueKeyMetadataIterator(dbmd, tableDefinition.getCatalog(), tableDefinition.getSchema(),
					tableDefinition.getTableName());
		} catch (SQLException e) {
			throw new JdbcFault(e);
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

	protected Iterator<String> defineAddUniqueKeys(final TableDefinition tableDefinition, final JdbcAdmin jdbcAdmin) {
		UniqueKeyDefinition[] ukDefinitions = tableDefinition.getUniqueKeyDefinitions();
		if (ukDefinitions != null) {
			final Iterator<Map.Entry<String, List<UniqueKeyDefinition>>> iterator = transferUniqueKeys(ukDefinitions).entrySet().iterator();
			return new Iterator<String>() {

				public boolean hasNext() {
					return iterator.hasNext();
				}

				public String next() {
					Map.Entry<String, List<UniqueKeyDefinition>> entry = iterator.next();
					return defineAddUniqueKey(tableDefinition, entry.getKey(), entry.getValue(), jdbcAdmin);
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}

			};
		}
		return null;
	}

	protected Iterator<String> defineUniqueKeys(final TableDefinition tableDefinition, final JdbcAdmin jdbcAdmin) {
		UniqueKeyDefinition[] ukDefinitions = tableDefinition.getUniqueKeyDefinitions();
		if (ukDefinitions != null) {
			final Iterator<Map.Entry<String, List<UniqueKeyDefinition>>> iterator = transferUniqueKeys(ukDefinitions).entrySet().iterator();
			return new Iterator<String>() {

				public boolean hasNext() {
					return iterator.hasNext();
				}

				public String next() {
					Map.Entry<String, List<UniqueKeyDefinition>> entry = iterator.next();
					return defineUniqueKey(tableDefinition, entry.getKey(), entry.getValue(), jdbcAdmin);
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}

			};
		}
		return null;
	}

	protected Iterator<String> defineAddForeignKeys(final TableDefinition tableDefinition, final JdbcAdmin jdbcAdmin) {
		ForeignKeyDefinition[] fkDefinitions = tableDefinition.getForeignKeyDefinitions();
		if (fkDefinitions != null) {
			final Iterator<Map.Entry<String, List<ForeignKeyDefinition>>> iterator = transferForeignKeys(fkDefinitions).entrySet()
					.iterator();
			return new Iterator<String>() {

				public boolean hasNext() {
					return iterator.hasNext();
				}

				public String next() {
					Map.Entry<String, List<ForeignKeyDefinition>> entry = iterator.next();
					return defineAddForeignKey(tableDefinition, entry.getKey(), entry.getValue(), jdbcAdmin);
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}
		return null;
	}

	protected Iterator<String> defineForeignKeys(final TableDefinition tableDefinition, final JdbcAdmin jdbcAdmin) {
		ForeignKeyDefinition[] fkDefinitions = tableDefinition.getForeignKeyDefinitions();
		if (fkDefinitions != null) {
			final Iterator<Map.Entry<String, List<ForeignKeyDefinition>>> iterator = transferForeignKeys(fkDefinitions).entrySet()
					.iterator();
			return new Iterator<String>() {

				public boolean hasNext() {
					return iterator.hasNext();
				}

				public String next() {
					Map.Entry<String, List<ForeignKeyDefinition>> entry = iterator.next();
					return defineForeignKey(tableDefinition, entry.getKey(), entry.getValue(), jdbcAdmin);
				}

				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}
		return null;
	}

	protected String defineDropForeignKey(TableDefinition tableDefinition, String constaintName, List<ForeignKeyDefinition> fkDefinitions,
			JdbcAdmin jdbcAdmin) {
		String[] args = { tableDefinition.getTableName(), matchForeignKeyName(tableDefinition, constaintName, fkDefinitions, jdbcAdmin) };
		return formatString(getDropForeignKeySqlString(), args);
	}

	private String matchForeignKeyName(TableDefinition tableDefinition, String constaintName, List<ForeignKeyDefinition> fkDefinitions,
			JdbcAdmin jdbcAdmin) {
		ForeignKeyDefinition firstDefinition = ListUtils.getFirst(fkDefinitions);
		TableDefinition refTableDefinition = jdbcAdmin.getConfiguration().getMetaData().getTable(firstDefinition.getRefMappedClass());
		if (StringUtils.isNotBlank(constaintName)) {
			Connection connection = jdbcAdmin.getConnectionProvider().openConnectionImplicitly();
			boolean constaintExists;
			try {
				constaintExists = JdbcUtils.foreignKeyExists(connection.getMetaData(), refTableDefinition.getCatalog(),
						refTableDefinition.getSchema(), refTableDefinition.getTableName(), constaintName);
			} catch (SQLException e) {
				throw new JdbcFault(e);
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
			iterator = JdbcUtils.foreignKeyMetadataIterator(connection.getMetaData(), refTableDefinition.getCatalog(),
					refTableDefinition.getSchema(), refTableDefinition.getTableName());
		} catch (SQLException e) {
			throw new JdbcFault(e);
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
						&& jdbcAdmin.getConfiguration().getMetaData()
								.getColumn(fkDefinition.getRefMappedClass(), fkDefinition.getRefMappedProperty()).getColumnName()
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
				constaintExists = JdbcUtils.primaryKeyExists(connection.getMetaData(), tableDefinition.getCatalog(),
						tableDefinition.getSchema(), tableDefinition.getTableName(), constaintName);
			} catch (SQLException e) {
				throw new JdbcFault(e);
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
			iterator = JdbcUtils.primaryKeyMetadataIterator(connection.getMetaData(), tableDefinition.getCatalog(),
					tableDefinition.getSchema(), tableDefinition.getTableName());
		} catch (SQLException e) {
			throw new JdbcFault(e);
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
				constaintExists = JdbcUtils.foreignKeyExists(connection.getMetaData(), tableDefinition.getCatalog(),
						tableDefinition.getSchema(), tableDefinition.getTableName(), constaintName);
			} catch (SQLException e) {
				throw new JdbcFault(e);
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
			iterator = JdbcUtils.uniqueKeyMetadataIterator(connection.getMetaData(), tableDefinition.getCatalog(),
					tableDefinition.getSchema(), tableDefinition.getTableName());
		} catch (SQLException e) {
			throw new JdbcFault(e);
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

	protected String defineAddPrimaryKey(TableDefinition tableDefinition) {
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
			return formatString(getAddPrimaryKeySqlString(), args);
		}
		return "";
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

	protected String[] defineAddColumn(ColumnDefinition columnDefinition) {
		TableDefinition tableDefinition = columnDefinition.getTableDefinition();
		String[] args = { tableDefinition.getTableName(), defineColumn(columnDefinition) };
		return new String[] { formatString(getAddColumnSqlString(), args) };
	}

	protected String[] defineModifyColumn(ColumnDefinition columnDefinition, int[] effects) {
		TableDefinition tableDefinition = columnDefinition.getTableDefinition();
		String[] args = { tableDefinition.getTableName(), defineColumn(columnDefinition) };
		return new String[] { formatString(getModifyColumnSqlString(), args) };
	}

	protected String[] defineDropColumn(ColumnDefinition columnDefinition) {
		TableDefinition tableDefinition = columnDefinition.getTableDefinition();
		String[] args = { tableDefinition.getTableName(), columnDefinition.getColumnName() };
		return new String[] { formatString(getDropColumnSqlString(), args) };
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
			refColumnNames.add(jdbcAdmin.getConfiguration().getMetaData()
					.getColumn(fkDefinition.getRefMappedClass(), fkDefinition.getRefMappedProperty()).getColumnName());
		}
		ForeignKeyDefinition fkDefinition = ListUtils.getFirst(fkDefinitions);
		TableDefinition reftTableDefinition = jdbcAdmin.getConfiguration().getMetaData().getTable(fkDefinition.getRefMappedClass());
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
			refColumnNames.add(jdbcAdmin.getConfiguration().getMetaData()
					.getColumn(fkDefinition.getRefMappedClass(), fkDefinition.getRefMappedProperty()).getColumnName());
		}
		ForeignKeyDefinition fkDefinition = ListUtils.getFirst(fkDefinitions);
		TableDefinition reftTableDefinition = jdbcAdmin.getConfiguration().getMetaData().getTable(fkDefinition.getRefMappedClass());
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

	public Query createQueryExecutor(Object... arguments) {
		try {
			return ConstructorUtils.invokeConstructor(QueryImpl.class, arguments);
		} catch (NoSuchMethodException e) {
			throw new SessionException(e.getMessage(), e);
		} catch (ReflectiveInvocationException e) {
			throw new SessionException(e.getMessage(), e);
		}
	}

}
