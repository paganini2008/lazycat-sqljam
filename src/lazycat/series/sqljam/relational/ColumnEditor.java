package lazycat.series.sqljam.relational;

/**
 * ColumnDefinition
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

	ColumnEditor setDefaultValue(String defaultValue);

	ColumnEditor setComment(String comment);

	ColumnEditor setColumnScript(String columnScript);

	ColumnEditor setInsertSql(String insertSql);

	PrimaryKeyEditor asPrimaryKey();

	ForeignKeyEditor asForeignKey(Class<?> refMappedClass, String refMappedProperty);

	UniqueKeyEditor asUniqueKey();

	ColumnEditor useIdentifierGenerator(String generatorName);

	ColumnEditor useSequence(String sequenceName);

	ColumnDefinition getColumnDefinition();

}