package lazycat.series.sqljam.relational;

import java.lang.reflect.Type;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.AutoDdl;

/**
 * TableEditor
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface TableEditor {

	TableEditor setAutoDdl(AutoDdl autoDdl);

	TableEditor setDdlContainsConstraint(boolean ddlContainsConstraint);

	ColumnEditor addColumn(String propertyName, Type javaType, String columnName, JdbcType jdbcType);

	TableEditor setComment(String comment);

	PrimaryKeyEditor addPrimaryKey(String propertyName);

	ForeignKeyEditor addForeignKey(String propertyName, Class<?> refMappedClass, String refMappedProperty);

	UniqueKeyEditor addUniqueKey(String propertyName);

	TableEditor useIdentifierGenerator(String propertyName, String generatorName);

	TableEditor useSequence(String propertyName, String sequenceName);

	TableDefinition getTableDefinition();

}
