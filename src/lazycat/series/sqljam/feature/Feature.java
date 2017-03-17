package lazycat.series.sqljam.feature;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lazycat.series.collection.CollectionUtils;
import lazycat.series.collection.ListUtils;
import lazycat.series.jdbc.JdbcType;
import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.DdlResolverException;
import lazycat.series.sqljam.JdbcAdmin;
import lazycat.series.sqljam.JdbcFault;
import lazycat.series.sqljam.JdbcTypeMapper;
import lazycat.series.sqljam.JdbcUtils;
import lazycat.series.sqljam.SQL92Criterion;
import lazycat.series.sqljam.SynaxFault;
import lazycat.series.sqljam.query.Query;
import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.ForeignKeyDefinition;
import lazycat.series.sqljam.relational.PrimaryKeyDefinition;
import lazycat.series.sqljam.relational.TableDefinition;
import lazycat.series.sqljam.relational.UniqueKeyDefinition;

/**
 * Abstract database product's feature
 * 
 * @author Fred Feng
 * @version 1.0
 */
public abstract class Feature extends SQL92Criterion {

	private final JdbcTypeMapper jdbcTypeMapper = new JdbcTypeMapper();
	private final Map<Type, JdbcType> javaTypeMapper = new HashMap<Type, JdbcType>();
	private final Map<String, String> functionMapper = new HashMap<String, String>();

	protected Feature() {
		registerGlobal();
	}

	protected void registerGlobal() {

		registerFunction("max", "max(?)");
		registerFunction("min", "min(?)");
		registerFunction("sum", "sum(?)");
		registerFunction("avg", "avg(?)");
		registerFunction("count", "count(?)");

		registerJavaType(Byte.TYPE, JdbcType.TINYINT);
		registerJavaType(Short.TYPE, JdbcType.SMALLINT);
		registerJavaType(Integer.TYPE, JdbcType.INTEGER);
		registerJavaType(Long.TYPE, JdbcType.BIGINT);
		registerJavaType(Float.TYPE, JdbcType.FLOAT);
		registerJavaType(Double.TYPE, JdbcType.DOUBLE);
		registerJavaType(Character.TYPE, JdbcType.CHAR);
		registerJavaType(Boolean.TYPE, JdbcType.BIT);

		registerJavaType(Byte.class, JdbcType.TINYINT);
		registerJavaType(Short.class, JdbcType.SMALLINT);
		registerJavaType(Integer.class, JdbcType.INTEGER);
		registerJavaType(Long.class, JdbcType.BIGINT);
		registerJavaType(Float.class, JdbcType.FLOAT);
		registerJavaType(Double.class, JdbcType.DOUBLE);
		registerJavaType(Character.class, JdbcType.CHAR);
		registerJavaType(Boolean.class, JdbcType.BIT);

		registerJavaType(String.class, JdbcType.VARCHAR);
		registerJavaType(BigDecimal.class, JdbcType.NUMERIC);
		registerJavaType(BigInteger.class, JdbcType.BIGINT);
		registerJavaType(Date.class, JdbcType.TIMESTAMP);
		registerJavaType(Timestamp.class, JdbcType.TIMESTAMP);
		registerJavaType(Time.class, JdbcType.TIME);
		registerJavaType(java.sql.Date.class, JdbcType.DATE);

		registerJavaType(byte[].class, JdbcType.BLOB);
		registerJavaType(char[].class, JdbcType.CLOB);
	}

	protected final void registerColumnType(JdbcType jdbcType, long capacity, String name) {
		jdbcTypeMapper.put(jdbcType, capacity, name);
	}

	protected final void registerColumnType(JdbcType jdbcType, String name) {
		jdbcTypeMapper.put(jdbcType, name);
	}

	protected final void registerFunction(String functionName, String functionTemplate) {
		functionMapper.put(functionName, functionTemplate);
	}

	protected final void registerJavaType(Type javaType, JdbcType jdbcType) {
		javaTypeMapper.put(javaType, jdbcType);
	}

	public String getColumnType(JdbcType jdbcType, int precision, int scale) {
		return jdbcTypeMapper.get(jdbcType, precision, scale);
	}

	public String getColumnType(JdbcType jdbcType, long length) {
		return jdbcTypeMapper.get(jdbcType, length);
	}

	public String getColumnType(JdbcType jdbcType) {
		return jdbcTypeMapper.get(jdbcType);
	}

	public String getFunctionTemplate(String functionName) {
		return functionMapper.get(functionName);
	}

	public JdbcType getJdbcType(Type javaType) {
		return javaTypeMapper.get(javaType);
	}

	protected boolean isAssigned(JdbcType fromType, JdbcType toType) {
		return JdbcUtils.isAssigned(fromType, toType);
	}

	public abstract String selectUUID();

	protected abstract String getAddTableCommentSqlString();

	protected abstract boolean isForeignKeyModified(ForeignKeyDefinition fkDefinition, JdbcAdmin jdbcAdmin, DatabaseMetaData dbmd);

	protected abstract boolean isPrimaryKeyModified(PrimaryKeyDefinition pkDefinition, DatabaseMetaData dbmd);

	protected abstract boolean isUniqueKeyModified(UniqueKeyDefinition uqDefinition, DatabaseMetaData dbmd);

	protected abstract int[] getColumnModified(ColumnDefinition columnDefinition, DatabaseMetaData dbmd);

	protected abstract Iterator<String> defineUniqueKeys(TableDefinition tableDefinition, JdbcAdmin jdbcAdmin);

	protected abstract Iterator<String> defineForeignKeys(TableDefinition tableDefinition, JdbcAdmin jdbcAdmin);

	protected abstract Iterator<String> defineAddForeignKeys(TableDefinition tableDefinition, JdbcAdmin jdbcAdmin);

	protected abstract Iterator<String> defineAddUniqueKeys(TableDefinition tableDefinition, JdbcAdmin jdbcAdmin);

	protected abstract String defineColumn(ColumnDefinition columnDefinition);

	protected abstract String[] defineAddColumn(ColumnDefinition columnDefinition);

	protected abstract String[] defineModifyColumn(ColumnDefinition columnDefinition, int[] effects);

	protected abstract String[] defineDropColumn(ColumnDefinition columnDefinition);

	protected abstract String defineAddUniqueKey(TableDefinition tableDefinition, String constaintName,
			List<UniqueKeyDefinition> uqDefinitions, JdbcAdmin jdbcAdmin);

	protected abstract String defineUniqueKey(TableDefinition tableDefinition, String constaintName,
			List<UniqueKeyDefinition> uqDefinitions, JdbcAdmin jdbcAdmin);

	protected abstract String defineDropUniqueKey(TableDefinition tableDefinition, String constaintName,
			List<UniqueKeyDefinition> uqDefinitions, JdbcAdmin jdbcAdmin);

	protected abstract String defineAddForeignKey(TableDefinition tableDefinition, String constaintName,
			List<ForeignKeyDefinition> fkDefinitions, JdbcAdmin jdbcAdmin);

	protected abstract String defineForeignKey(TableDefinition tableDefinition, String constaintName,
			List<ForeignKeyDefinition> fkDefinitions, JdbcAdmin jdbcAdmin);

	protected abstract String defineDropForeignKey(TableDefinition tableDefinition, String constaintName,
			List<ForeignKeyDefinition> fkDefinitions, JdbcAdmin jdbcAdmin);

	protected abstract String definePrimaryKey(TableDefinition tableDefinition);

	protected abstract String defineAddPrimaryKey(TableDefinition tableDefinition);

	protected abstract String defineDropPrimaryKey(TableDefinition tableDefinition, String constaintName,
			List<PrimaryKeyDefinition> pkDefinitions, JdbcAdmin jdbcAdmin);

	protected final Map<String, List<ForeignKeyDefinition>> transferForeignKeys(ForeignKeyDefinition[] definitions) {
		Map<String, List<ForeignKeyDefinition>> mapper = new HashMap<String, List<ForeignKeyDefinition>>();
		for (ForeignKeyDefinition fkDefinition : definitions) {
			if (fkDefinition.isRequired()) {
				List<ForeignKeyDefinition> list = mapper.get(fkDefinition.getConstraintName());
				if (list == null) {
					mapper.put(fkDefinition.getConstraintName(), new ArrayList<ForeignKeyDefinition>());
					list = mapper.get(fkDefinition.getConstraintName());
				}
				list.add(fkDefinition);
			}
		}
		for (List<ForeignKeyDefinition> fkDefinitions : mapper.values()) {
			Set<Class<?>> refMappedClasses = new HashSet<Class<?>>();
			for (ForeignKeyDefinition fkDefinition : fkDefinitions) {
				refMappedClasses.add(fkDefinition.getRefMappedClass());
			}
			if (refMappedClasses.size() != 1) {
				throw new SynaxFault("Constaint name reference different tables.");
			}
		}
		return mapper;
	}

	protected final Map<String, List<UniqueKeyDefinition>> transferUniqueKeys(UniqueKeyDefinition[] definitions) {
		Map<String, List<UniqueKeyDefinition>> mapper = new HashMap<String, List<UniqueKeyDefinition>>();
		for (UniqueKeyDefinition uqDefinition : definitions) {
			List<UniqueKeyDefinition> list = mapper.get(uqDefinition.getConstraintName());
			if (list == null) {
				mapper.put(uqDefinition.getConstraintName(), new ArrayList<UniqueKeyDefinition>());
				list = mapper.get(uqDefinition.getConstraintName());
			}
			list.add(uqDefinition);
		}
		return mapper;
	}

	public Iterator<String> iteratorForUpdateDDL(TableDefinition tableDefinition, JdbcAdmin jdbcAdmin, DatabaseMetaData dbmd) {
		ColumnDefinition[] columnDefinitions = tableDefinition.getColumnDefinitions();
		final List<String> ddls = new ArrayList<String>();
		for (ColumnDefinition columnDefinition : columnDefinitions) {
			try {
				if (JdbcUtils.columnExists(dbmd, tableDefinition.getCatalog(), tableDefinition.getSchema(), tableDefinition.getTableName(),
						columnDefinition.getColumnName())) {
					int[] effects = getColumnModified(columnDefinition, dbmd);
					if (effects != null) {
						ddls.addAll(Arrays.asList(defineModifyColumn(columnDefinition, effects)));
					}
				} else {
					ddls.addAll(Arrays.asList(defineAddColumn(columnDefinition)));
				}
			} catch (SQLException e) {
				throw new JdbcFault(e);
			}

		}

		if (tableDefinition.hasPrimaryKey()) {
			PrimaryKeyDefinition[] definitions = tableDefinition.getPrimaryKeyDefinitions();
			for (PrimaryKeyDefinition pkDefinition : definitions) {
				if (isPrimaryKeyModified(pkDefinition, dbmd)) {
					ddls.add(defineDropPrimaryKey(tableDefinition, pkDefinition.getConstraintName(), ListUtils.create(definitions),
							jdbcAdmin));
					ddls.add(defineAddPrimaryKey(tableDefinition));
					break;
				}
			}
		}

		if (tableDefinition.hasForeignKey()) {
			for (Map.Entry<String, List<ForeignKeyDefinition>> entry : transferForeignKeys(tableDefinition.getForeignKeyDefinitions())
					.entrySet()) {
				for (ForeignKeyDefinition fkDefinition : entry.getValue()) {
					if (isForeignKeyModified(fkDefinition, jdbcAdmin, dbmd)) {
						ddls.add(defineDropForeignKey(tableDefinition, fkDefinition.getConstraintName(), entry.getValue(), jdbcAdmin));
						ddls.add(defineAddForeignKey(tableDefinition, fkDefinition.getConstraintName(), entry.getValue(), jdbcAdmin));
						break;
					}
				}
			}
		}

		if (tableDefinition.hasUniqueKey()) {
			for (Map.Entry<String, List<UniqueKeyDefinition>> entry : transferUniqueKeys(tableDefinition.getUniqueKeyDefinitions())
					.entrySet()) {
				for (UniqueKeyDefinition uqDefinition : entry.getValue()) {
					if (isUniqueKeyModified(uqDefinition, dbmd)) {
						ddls.add(defineDropUniqueKey(tableDefinition, uqDefinition.getConstraintName(), entry.getValue(), jdbcAdmin));
						ddls.add(defineAddUniqueKey(tableDefinition, uqDefinition.getConstraintName(), entry.getValue(), jdbcAdmin));
						break;
					}
				}
			}
		}

		return ddls.iterator();

	}

	public void validateTable(TableDefinition tableDefinition, JdbcAdmin jdbcAdmin, DatabaseMetaData dbmd) {
		ColumnDefinition[] columnDefinitions = tableDefinition.getColumnDefinitions();
		for (ColumnDefinition columnDefinition : columnDefinitions) {
			try {
				if (JdbcUtils.columnExists(dbmd, tableDefinition.getCatalog(), tableDefinition.getSchema(), tableDefinition.getTableName(),
						columnDefinition.getColumnName())) {
					if (getColumnModified(columnDefinition, dbmd) != null) {
						throw new DdlResolverException("Column '" + columnDefinition.getColumnName() + "' is modified.");
					}
				} else {
					throw new DdlResolverException("Column '" + columnDefinition.getColumnName() + "' is not existed.");
				}
			} catch (SQLException e) {
				throw new JdbcFault(e);
			}

		}

		PrimaryKeyDefinition[] pkDefinitions = tableDefinition.getPrimaryKeyDefinitions();
		for (PrimaryKeyDefinition pkDefinition : pkDefinitions) {
			if (isPrimaryKeyModified(pkDefinition, dbmd)) {
				throw new DdlResolverException("PrimaryKey '" + pkDefinition.getColumnDefinition().getColumnName() + "' is modified.");
			}
		}

		for (Map.Entry<String, List<ForeignKeyDefinition>> entry : transferForeignKeys(tableDefinition.getForeignKeyDefinitions())
				.entrySet()) {
			for (ForeignKeyDefinition fkDefinition : entry.getValue()) {
				if (isForeignKeyModified(fkDefinition, jdbcAdmin, dbmd)) {
					throw new DdlResolverException("ForeignKey '" + fkDefinition.getColumnDefinition().getColumnName() + "' is modified.");
				}
			}
		}

		for (Map.Entry<String, List<UniqueKeyDefinition>> entry : transferUniqueKeys(tableDefinition.getUniqueKeyDefinitions())
				.entrySet()) {
			for (UniqueKeyDefinition uqDefinition : entry.getValue()) {
				if (isUniqueKeyModified(uqDefinition, dbmd)) {
					throw new DdlResolverException("UniqueKey '" + uqDefinition.getColumnDefinition().getColumnName() + "' is modified.");
				}
			}
		}
	}

	public Iterator<String> iteratorForCreateDDL(TableDefinition tableDefinition, JdbcAdmin jdbcAdmin, DatabaseMetaData dbmd) {
		ColumnDefinition[] definitions = tableDefinition.getColumnDefinitions();
		final List<String> parts = new ArrayList<String>();
		final List<String> ddls = new ArrayList<String>();
		String part;
		for (ColumnDefinition columnDefinition : definitions) {
			part = defineColumn(columnDefinition);
			if (StringUtils.isNotBlank(part)) {
				parts.add(part);
			}
		}
		if (tableDefinition.isDdlContainsConstraint()) {
			part = definePrimaryKey(tableDefinition);
			if (StringUtils.isNotBlank(part)) {
				parts.add(part);
			}
			Iterator<String> iter = defineForeignKeys(tableDefinition, jdbcAdmin);
			if (iter != null) {
				for (; iter.hasNext();) {
					parts.add(iter.next());
				}
			}
			iter = defineUniqueKeys(tableDefinition, jdbcAdmin);
			if (iter != null) {
				for (; iter.hasNext();) {
					parts.add(iter.next());
				}
			}
		}
		beforeCreateTable(tableDefinition, parts, ddls);
		StringBuilder createSql = new StringBuilder();
		createSql.append(tableDefinition.getTableName());
		createSql.append(" (");
		createSql.append(CollectionUtils.join(parts, ","));
		createSql.append(")");
		ddls.add(createTable(createSql.toString()));

		if (StringUtils.isNotBlank(tableDefinition.getComment())) {
			ddls.add(formatString(getAddTableCommentSqlString(), tableDefinition.getTableName(), tableDefinition.getComment()));
		}

		if (!tableDefinition.isDdlContainsConstraint()) {
			part = defineAddPrimaryKey(tableDefinition);
			ddls.add(part);
			Iterator<String> iter = defineAddForeignKeys(tableDefinition, jdbcAdmin);
			if (iter != null) {
				for (; iter.hasNext();) {
					ddls.add(iter.next());
				}
			}
			iter = defineAddUniqueKeys(tableDefinition, jdbcAdmin);
			if (iter != null) {
				for (; iter.hasNext();) {
					ddls.add(iter.next());
				}
			}
		}
		afterCreateTable(tableDefinition, ddls);
		return ddls.iterator();
	}

	public Iterator<String> iteratorForDropDDL(TableDefinition tableDefinition, DatabaseMetaData dbmd) {
		List<String> ddls = new ArrayList<String>();
		beforeDropTable(tableDefinition, ddls);
		ddls.add(dropTable(tableDefinition.getTableName()));
		afterDropTable(tableDefinition, ddls);
		return ddls.iterator();
	}

	static String formatString(String pattern, String... args) {
		return StringUtils.parseText(pattern, "?", args);
	}

	protected void beforeCreateTable(TableDefinition tableDefinition, List<String> fragments, List<String> ddls) {
	}

	protected void afterCreateTable(TableDefinition tableDefinition, List<String> ddls) {
	}

	protected void afterDropTable(TableDefinition tableDefinition, List<String> ddls) {
	}

	protected void beforeDropTable(TableDefinition tableDefinition, List<String> ddls) {
	}

	public boolean makeSureHaveRowsWhenAlterTable() {
		return false;
	}

	protected boolean jdbcTypeCheckedStrictly() {
		return true;
	}

	public boolean selectCurrvalIfNecessary() {
		return true;
	}

	public String decorateQueryColumnAlias(String alias) {
		return alias;
	}

	public abstract String getPageableSqlString(String sql, int offset, int limit);

	public abstract Query createQueryExecutor(Object... arguments);

}
