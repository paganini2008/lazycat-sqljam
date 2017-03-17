package lazycat.series.sqljam.relational;

import lazycat.series.sqljam.AutoDdl;
import lazycat.series.sqljam.generator.IdentifierGenerator;

/**
 * TableDefinition
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface TableDefinition {

	String getCatalog();

	String getSchema();

	String getComment();

	AutoDdl getAutoDdl();

	Class<?> getMappedClass();

	String getTableName();

	boolean isDdlContainsConstraint();

	ColumnDefinition getColumn(String mappedProperty);

	boolean hasProperty(String mappedProperty);

	ColumnDefinition[] getColumnDefinitions();

	boolean hasPrimaryKey();

	boolean hasForeignKey();

	boolean hasUniqueKey();

	boolean isPrimaryKey(String mappedProperty);

	PrimaryKeyDefinition getPrimaryKeyDefinition();

	boolean isAutoIncrement(String mappedProperty);

	UniqueKeyDefinition[] getUniqueKeyDefinitions();

	ForeignKeyDefinition[] getForeignKeyDefinitions(Class<?> mappedClass);

	ForeignKeyDefinition[] getForeignKeyDefinitions();

	PrimaryKeyDefinition[] getPrimaryKeyDefinitions();

	IdentifierGenerator getSequenceGenerator(String propertyName);

	IdentifierGenerator getUserDefinedGenerator(String propertyName);

	String getFullTableName();

	SchemaDefinition getSchemaDefinition();

}