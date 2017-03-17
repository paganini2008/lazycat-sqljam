package lazycat.series.sqljam.relational;

/**
 * PrimaryKeyEditor
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface PrimaryKeyEditor {

	PrimaryKeyDefinition getPrimaryKeyDefinition();

	PrimaryKeyEditor setPosition(int position);

	PrimaryKeyEditor setConstraintName(String constraintName);

}