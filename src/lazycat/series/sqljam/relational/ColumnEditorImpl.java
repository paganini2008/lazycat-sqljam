package lazycat.series.sqljam.relational;

import java.lang.reflect.Type;

import lazycat.series.jdbc.JdbcType;

/**
 * ColumnEditorImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ColumnEditorImpl implements ColumnEditor {

	private final TableEditor tableEditor;
	private final StandardColumnDefinition columnDefinition;

	ColumnEditorImpl(TableEditor tableEditor, String mappedProperty, Type javaType, String columnName, JdbcType jdbcType) {
		this.columnDefinition = new StandardColumnDefinition(tableEditor.getTableDefinition(), mappedProperty, javaType,
				columnName, jdbcType);
		this.tableEditor = tableEditor;
	}

	public TableEditor getTable() {
		return tableEditor;
	}

	public ColumnEditor setUnsigned(boolean unsigned) {
		this.columnDefinition.setUnsigned(unsigned);
		return this;
	}

	public ColumnEditor setLength(long length) {
		this.columnDefinition.setLength(length);
		return this;
	}

	public ColumnEditor setPrecision(int precision) {
		this.columnDefinition.setPrecision(precision);
		return this;
	}

	public ColumnEditor setScale(int scale) {
		this.columnDefinition.setScale(scale);
		return this;
	}

	public ColumnEditor setNullable(boolean nullable) {
		this.columnDefinition.setNullable(nullable);
		return this;
	}

	public ColumnEditor setAutoIncrement(boolean autoIncrement) {
		this.columnDefinition.setAutoIncrement(autoIncrement);
		return this;
	}

	public ColumnEditor setComment(String comment) {
		this.columnDefinition.setComment(comment);
		return this;
	}

	public ColumnEditor setDefaultValue(String defaultValue) {
		this.columnDefinition.setDefaultValue(defaultValue);
		return this;
	}

	public PrimaryKeyEditor asPrimaryKey() {
		return tableEditor.addPrimaryKey(columnDefinition.getMappedProperty());
	}

	public ForeignKeyEditor asForeignKey(Class<?> refMappedClass, String refMappedProperty) {
		return tableEditor.addForeignKey(columnDefinition.getMappedProperty(), refMappedClass, refMappedProperty);
	}

	public UniqueKeyEditor asUniqueKey() {
		return tableEditor.addUniqueKey(columnDefinition.getMappedProperty());
	}

	public DefaultEditor addDefault() {
		return tableEditor.addDefault(columnDefinition.getMappedProperty());
	}

	public ColumnEditor useGenerator(String generatorType, String name) {
		tableEditor.useGenerator(columnDefinition.getMappedProperty(), generatorType, name);
		return this;
	}

	public ColumnEditor useGenerator(String generatorType) {
		tableEditor.useGenerator(columnDefinition.getMappedProperty(), generatorType);
		return this;
	}

	public ColumnDefinition getColumnDefinition() {
		return columnDefinition;
	}

}
