package lazycat.series.sqljam.relational;

import lazycat.series.beans.ToStringBuilder;
import lazycat.series.sqljam.CascadeAction;

/**
 * ForeignKey Definition
 * 
 * @author Fred Feng
 * @see StandardPrimaryKeyDefinition
 * @see StandardUniqueKeyDefinition
 * @see StandardColumnDefinition
 */
public class StandardForeignKeyDefinition implements ForeignKeyDefinition {

	private final ColumnDefinition columnDefinition;
	private final String refMappedProperty;
	private final Class<?> refMappedClass;
	private String constraintName;
	private int position;
	private boolean required;
	private CascadeAction onDelete;
	private CascadeAction onUpdate;

	StandardForeignKeyDefinition(ColumnDefinition columnDefinition, Class<?> refMappedClass, String refMappedProperty) {
		this.columnDefinition = columnDefinition;
		this.refMappedClass = refMappedClass;
		this.refMappedProperty = refMappedProperty;
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

	public String getRefMappedProperty() {
		return refMappedProperty;
	}

	public Class<?> getRefMappedClass() {
		return refMappedClass;
	}

	public ColumnDefinition getColumnDefinition() {
		return columnDefinition;
	}

	public int getPosition() {
		return position;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public CascadeAction getOnDelete() {
		return onDelete;
	}

	public void setOnDelete(CascadeAction onDelete) {
		this.onDelete = onDelete;
	}

	public CascadeAction getOnUpdate() {
		return onUpdate;
	}

	public void setOnUpdate(CascadeAction onUpdate) {
		this.onUpdate = onUpdate;
	}

	public String toString() {
		return ToStringBuilder.reflectInvokeToString(this);
	}

	public int compareTo(ForeignKeyDefinition other) {
		return getPosition() - other.getPosition();
	}

}
