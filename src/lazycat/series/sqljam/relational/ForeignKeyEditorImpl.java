package lazycat.series.sqljam.relational;

import lazycat.series.sqljam.CascadeAction;

/**
 * ForeignKeyEditorImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ForeignKeyEditorImpl implements ForeignKeyEditor {

	private final StandardForeignKeyDefinition foreignKeyDefinition;

	ForeignKeyEditorImpl(TableEditor tableEditor, String propertyName, Class<?> refMappedClass, String refMappedProperty) {
		this.foreignKeyDefinition = new StandardForeignKeyDefinition(tableEditor.getTableDefinition().getColumnDefinition(propertyName),
				refMappedClass, refMappedProperty);
	}

	public ForeignKeyDefinition getForeignKeyDefinition() {
		return foreignKeyDefinition;
	}

	public ForeignKeyEditor setPosition(int position) {
		this.foreignKeyDefinition.setPosition(position);
		return this;
	}

	public ForeignKeyEditor setConstraintName(String constraintName) {
		this.foreignKeyDefinition.setConstraintName(constraintName);
		return this;
	}

	public ForeignKeyEditor setRequired(boolean required) {
		this.foreignKeyDefinition.setRequired(required);
		return this;
	}

	public ForeignKeyEditor setOnUpdate(CascadeAction onUpdate) {
		this.foreignKeyDefinition.setOnUpdate(onUpdate);
		return this;
	}

	public ForeignKeyEditor setOnDelete(CascadeAction onDelete) {
		this.foreignKeyDefinition.setOnDelete(onDelete);
		return this;
	}

}
