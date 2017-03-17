package lazycat.series.sqljam.relational;

/**
 * UniqueKeyDefinition
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface UniqueKeyDefinition extends Comparable<UniqueKeyDefinition> {

	ColumnDefinition getColumnDefinition();

	String getConstraintName();

	int getPosition();

}