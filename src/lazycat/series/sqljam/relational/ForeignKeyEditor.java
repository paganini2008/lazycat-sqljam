package lazycat.series.sqljam.relational;

import lazycat.series.sqljam.CascadeAction;

/**
 * ForeignKeyEditor
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface ForeignKeyEditor {

	ForeignKeyEditor setConstraintName(String constraintName);

	ForeignKeyEditor setRequired(boolean required);

	ForeignKeyEditor setPosition(int position);

	ForeignKeyEditor setOnUpdate(CascadeAction onUpdate);

	ForeignKeyEditor setOnDelete(CascadeAction onDelete);

	ForeignKeyDefinition getForeignKeyDefinition();

}