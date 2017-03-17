package lazycat.series.sqljam.relational;

import lazycat.series.sqljam.CascadeAction;

/**
 * ForeignKeyDefinition
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface ForeignKeyDefinition extends Comparable<ForeignKeyDefinition> {

	String getConstraintName();

	String getRefMappedProperty();

	Class<?> getRefMappedClass();

	ColumnDefinition getColumnDefinition();

	boolean isRequired();
	
	int getPosition();
	
	CascadeAction getOnDelete();
	
	CascadeAction getOnUpdate();

}