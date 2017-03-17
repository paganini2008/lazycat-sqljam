package lazycat.series.sqljam.relational;

import lazycat.series.beans.ToStringBuilder;

/**
 * A description of a unique key
 * 
 * @author Fred Feng
 * @see PrimaryKeyDefinition
 * @see ForeignKeyDefinition
 * @see ColumnDefinition
 */
public class StandardUniqueKeyDefinition implements UniqueKeyDefinition {

	private final ColumnDefinition columnDefinition;
	private String constraintName;
	private int position;

	public StandardUniqueKeyDefinition(ColumnDefinition columnDefinition) {
		this.columnDefinition = columnDefinition;
	}

	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getConstraintName() {
		return constraintName;
	}

	public int getPosition() {
		return position;
	}
	
	public ColumnDefinition getColumnDefinition() {
		return columnDefinition;
	}

	public int compareTo(UniqueKeyDefinition other) {
		return this.getPosition() - other.getPosition();
	}

	public String toString() {
		return ToStringBuilder.reflectInvokeToString(this);
	}

}
