package lazycat.series.sqljam.relational;

import java.lang.reflect.Field;

import lazycat.series.beans.ToStringBuilder;
import lazycat.series.sqljam.annotation.PrimaryKey;

/**
 * PrimaryKey Definition
 * 
 * @author Fred Feng
 * @see StandardColumnDefinition
 * @see StandardForeignKeyDefinition
 * @see StandardUniqueKeyDefinition
 */
public class StandardPrimaryKeyDefinition implements PrimaryKeyDefinition {

	private final ColumnDefinition columnDefinition;
	private String constraintName;
	private int position;

	public StandardPrimaryKeyDefinition(ColumnDefinition columnDefinition) {
		this.columnDefinition = columnDefinition;
	}

	StandardPrimaryKeyDefinition(ColumnDefinition columnDefinition, Field field) {
		this.columnDefinition = columnDefinition;
		PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
		this.constraintName = primaryKey.name();
		this.position = primaryKey.position();
	}

	public ColumnDefinition getColumnDefinition() {
		return columnDefinition;
	}

	public String getConstraintName() {
		return constraintName;
	}

	public int getPosition() {
		return position;
	}

	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int compareTo(PrimaryKeyDefinition other) {
		return getPosition() - other.getPosition();
	}

	public String toString() {
		return ToStringBuilder.reflectInvokeToString(this);
	}

}
