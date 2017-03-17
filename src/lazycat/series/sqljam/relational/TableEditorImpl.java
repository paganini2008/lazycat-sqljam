package lazycat.series.sqljam.relational;

import java.lang.reflect.Type;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.AutoDdl;
import lazycat.series.sqljam.generator.IdentifierGenerator;

/**
 * TableEditorImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class TableEditorImpl implements TableEditor {

	private final StardardTableDefinition tableDefinition;

	public TableEditorImpl(SchemaEditor schemaEditor, Class<?> mappedClass, String tableName) {
		this.tableDefinition = new StardardTableDefinition(schemaEditor.getSchemaDefinition(), mappedClass, tableName);
	}

	public TableEditor setDdlContainsConstraint(boolean ddlContainsConstraint) {
		tableDefinition.setDdlContainsConstraint(ddlContainsConstraint);
		return this;
	}

	public TableEditor setComment(String comment) {
		tableDefinition.setComment(comment);
		return this;
	}

	public TableEditor setAutoDdl(AutoDdl autoDdl) {
		tableDefinition.setAutoDdl(autoDdl);
		return this;
	}

	public TableEditor useIdentifierGenerator(String propertyName, String generatorName) {
		((StandardColumnDefinition) tableDefinition.columns.get(propertyName))
				.setIdentifierGenerator(tableDefinition.getSchemaDefinition().getIdentifierGenerator(generatorName));
		return this;
	}

	public TableEditor useSequence(String propertyName, String sequenceName) {
		IdentifierGenerator identifierGenerator = tableDefinition.getSchemaDefinition().getSequence(sequenceName)
				.getIdentifierGenerator();
		((StandardColumnDefinition) tableDefinition.columns.get(propertyName)).setIdentifierGenerator(identifierGenerator);
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
		return foreignKeyEditor;
	}

	public UniqueKeyEditor addUniqueKey(String propertyName) {
		UniqueKeyEditor uniqueKeyEditor = new UniqueKeyEditorImpl(this, propertyName);
		tableDefinition.uniqueKeys.put(propertyName, uniqueKeyEditor.getUniqueKeyDefinition());
		return uniqueKeyEditor;
	}

	public TableDefinition getTableDefinition() {
		return tableDefinition;
	}

}
