package lazycat.series.sqljam.relational;

/**
 * UniqueKeyEditorImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class UniqueKeyEditorImpl implements UniqueKeyEditor {

	private final StandardUniqueKeyDefinition uniqueKeyDefinition;

	UniqueKeyEditorImpl(TableEditor tableEditor, String mappedProperty) {
		this.uniqueKeyDefinition = new StandardUniqueKeyDefinition(tableEditor.getTableDefinition().getColumnDefinition(mappedProperty));
	}

	public UniqueKeyEditor setConstraintName(String constraintName) {
		this.uniqueKeyDefinition.setConstraintName(constraintName);
		return this;
	}

	public UniqueKeyEditor setPosition(int position) {
		this.uniqueKeyDefinition.setPosition(position);
		return this;
	}

	public UniqueKeyDefinition getUniqueKeyDefinition() {
		return uniqueKeyDefinition;
	}
}
