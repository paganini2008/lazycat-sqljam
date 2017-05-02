package lazycat.series.sqljam.relational;

import lazycat.series.sqljam.expression.DataType;

/**
 * StandardDefaultDefinition
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class StandardDefaultDefinition implements DefaultDefinition {

	private final ColumnDefinition columnDefinition;
	private String value;
	private DataType dataType;

	public StandardDefaultDefinition(ColumnDefinition columnDefinition) {
		this.columnDefinition = columnDefinition;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public ColumnDefinition getColumnDefinition() {
		return columnDefinition;
	}

	public String getValue() {
		return value;
	}

	public DataType getDataType() {
		return dataType;
	}

}
