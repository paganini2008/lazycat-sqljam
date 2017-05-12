package lazycat.series.sqljam.relational;

import java.lang.reflect.Type;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.AutoDdl;
import lazycat.series.sqljam.JdbcAdmin;

/**
 * TableEditorImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class TableEditorImpl implements TableEditor {

	protected final JdbcAdmin jdbcAdmin;
	private final StardardTableDefinition tableDefinition;

	public TableEditorImpl(Class<?> mappedClass, JdbcAdmin jdbcAdmin) {
		this.tableDefinition = new StardardTableDefinition(mappedClass);
		this.jdbcAdmin = jdbcAdmin;
	}

	public TableEditor setName(String name) {
		return setName(null, name);
	}

	public TableEditor setName(String schema, String name) {
		SchemaEditor schemaEditor = jdbcAdmin.getConfiguration().getSchemaEditor(schema);
		tableDefinition.setSchemaDefinition(schemaEditor.getSchemaDefinition());
		tableDefinition.setTableName(name);
		return this;
	}

	public TableEditor setAutoDdl(AutoDdl autoDdl) {
		tableDefinition.setAutoDdl(autoDdl);
		return this;
	}

	public TableEditor setDefineConstraintOnCreate(boolean defineConstraintOnCreate) {
		tableDefinition.setDefineConstraintOnCreate(defineConstraintOnCreate);
		return this;
	}

	public TableEditor setComment(String comment) {
		tableDefinition.setComment(comment);
		return this;
	}

	public ColumnEditor addColumn(String propertyName, Type javaType, String columnName, JdbcType jdbcType) {
		ColumnEditor columnEditor = new ColumnEditorImpl(this, propertyName, javaType, columnName, jdbcType);
		tableDefinition.columns.put(propertyName, columnEditor.getColumnDefinition());
		return columnEditor;
	}

	public PrimaryKeyEditor addPrimaryKey(String propertyName) {
		PrimaryKeyEditor primaryKeyEditor = new PrimaryKeyEditorImpl(this, propertyName);
		tableDefinition.primaryKeys.put(propertyName, primaryKeyEditor.getPrimaryKeyDefinition());
		return primaryKeyEditor;
	}

	public ForeignKeyEditor addForeignKey(String propertyName, Class<?> refMappedClass, String refMappedProperty) {
		ForeignKeyEditor foreignKeyEditor = new ForeignKeyEditorImpl(this, propertyName, refMappedClass, refMappedProperty);
		tableDefinition.foreignKeys.put(propertyName, foreignKeyEditor.getForeignKeyDefinition());
		TableDefinition refTableDefinition = jdbcAdmin.getConfiguration().getTableDefinition(refMappedClass);
		((StardardTableDefinition) refTableDefinition).references.add(tableDefinition);
		return foreignKeyEditor;
	}

	public UniqueKeyEditor addUniqueKey(String propertyName) {
		UniqueKeyEditor uniqueKeyEditor = new UniqueKeyEditorImpl(this, propertyName);
		tableDefinition.uniqueKeys.put(propertyName, uniqueKeyEditor.getUniqueKeyDefinition());
		return uniqueKeyEditor;
	}

	public DefaultEditor addDefault(String propertyName) {
		DefaultEditor defaultEditor = new DefaultEditorImpl(this, propertyName);
		tableDefinition.defaults.put(propertyName, defaultEditor.getDefaultDefinition());
		return defaultEditor;
	}

	public TableDefinition getTableDefinition() {
		return tableDefinition;
	}

}
