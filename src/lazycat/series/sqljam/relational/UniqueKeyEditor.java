package lazycat.series.sqljam.relational;

/**
 * UniqueKeyEditor
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface UniqueKeyEditor {

	UniqueKeyDefinition getUniqueKeyDefinition();

	UniqueKeyEditor setConstraintName(String constraintName);

	UniqueKeyEditor setPosition(int position);

}