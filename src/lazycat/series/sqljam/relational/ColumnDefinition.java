package lazycat.series.sqljam.relational;

import java.lang.reflect.Type;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.generator.Generator;

/**
 * ColumnDefinition
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface ColumnDefinition {

	String getColumnName();

	JdbcType getJdbcType();

	long getLength();

	int getPrecision();

	int getScale();

	boolean isNullable();

	boolean isAutoIncrement();

	boolean isUnsigned();

	String getComment();

	String getDefaultValue();

	String getMappedProperty();

	Type getJavaType();

	TableDefinition getTableDefinition();
	
	Generator getGenerator();

}