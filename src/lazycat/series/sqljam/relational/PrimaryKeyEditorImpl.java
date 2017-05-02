package lazycat.series.sqljam.relational;

/**
 * PrimaryKeyDefinitionImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class PrimaryKeyEditorImpl implements PrimaryKeyEditor {

	private final StandardPrimaryKeyDefinition primaryKeyDefinition;
	private String constraintName;
	private int position;

	PrimaryKeyEditorImpl(TableEditor tableEditor, String propertyName) {
		this.primaryKeyDefinition = new StandardPrimaryKeyDefinition(tableEditor.getTableDefinition().getColumnDefinition(propertyName));
	}

	public PrimaryKeyEditor setConstraintName(String constraintName) {
		this.primaryKeyDefinition.setConstraintName(constraintName);
		return this;
	}

	public String getConstraintName() {
		return constraintName;
	}

	public PrimaryKeyEditor setPosition(int position) {
		this.position = position;
		return this;
	}

	public int getPosition() {
		return position;
	}

	public PrimaryKeyDefinition getPrimaryKeyDefinition() {
		return primaryKeyDefinition;
	}

}
