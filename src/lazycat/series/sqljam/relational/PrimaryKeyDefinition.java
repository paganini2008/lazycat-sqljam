package lazycat.series.sqljam.relational;

/**
 * PrimaryKeyCriteria
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface PrimaryKeyDefinition extends Comparable<PrimaryKeyDefinition> {

	ColumnDefinition getColumnDefinition();

	String getConstraintName();

	int getPosition();

}