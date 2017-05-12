package lazycat.series.sqljam.relational;

import lazycat.series.sqljam.AutoDdl;

/**
 * TableDefinition
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface TableDefinition {

	AutoDdl getAutoDdl();

	String getComment();

	Class<?> getMappedClass();

	String getTableName();

	boolean isDefineConstraintOnCreate();

	ColumnDefinition getColumnDefinition(String property);

	boolean hasProperty(String property);

	ColumnDefinition[] getColumnDefinitions();

	boolean hasPrimaryKey();

	boolean hasForeignKey();

	boolean hasUniqueKey();

	boolean isPrimaryKey(String property);

	PrimaryKeyDefinition getPrimaryKeyDefinition();

	boolean isAutoIncrement(String property);

	UniqueKeyDefinition[] getUniqueKeyDefinitions();

	ForeignKeyDefinition[] getForeignKeyDefinitions(Class<?> mappedClass);

	ForeignKeyDefinition[] getForeignKeyDefinitions();

	PrimaryKeyDefinition[] getPrimaryKeyDefinitions();

	DefaultDefinition getDefaultDefinition(String property);

	String getSchema();

	SchemaDefinition getSchemaDefinition();

	TableDefinition[] getReferences();

}