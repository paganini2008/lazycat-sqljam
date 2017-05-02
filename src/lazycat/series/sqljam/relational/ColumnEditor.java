package lazycat.series.sqljam.relational;

/**
 * ColumnEditor
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface ColumnEditor {

	ColumnEditor setLength(long length);

	ColumnEditor setPrecision(int precision);

	ColumnEditor setScale(int scale);

	ColumnEditor setNullable(boolean nullable);

	ColumnEditor setAutoIncrement(boolean autoincrement);

	ColumnEditor setUnsigned(boolean unsigned);

	ColumnEditor setComment(String comment);

	ColumnEditor setDefaultValue(String defaultValue);

	PrimaryKeyEditor asPrimaryKey();

	ForeignKeyEditor asForeignKey(Class<?> refMappedClass, String refMappedProperty);

	UniqueKeyEditor asUniqueKey();

	DefaultEditor addDefault();

	ColumnEditor useGenerator(String generatorType, String name);

	ColumnEditor useGenerator(String generatorType);

	ColumnDefinition getColumnDefinition();

}