package lazycat.series.sqljam.relational;

import java.lang.reflect.Type;

import lazycat.series.beans.PropertyFilters;
import lazycat.series.beans.ToStringBuilder;
import lazycat.series.jdbc.JdbcType;

/**
 * A description of table column
 * 
 * @author Fred Feng
 * @version 1.0
 * @see StardardTableDefinition
 */
public class StandardColumnDefinition implements ColumnDefinition {

	private final TableDefinition tableDefinition;
	private final String mappedProperty;
	private final Type javaType;
	private final String columnName;
	private final JdbcType jdbcType;
	private boolean unsigned;
	private long length;
	private int precision;
	private int scale;
	private boolean nullable;
	private boolean autoIncrement;
	private String comment;
	private String defaultValue;

	StandardColumnDefinition(TableDefinition tableDefinition, String mappedProperty, Type javaType, String columnName, JdbcType jdbcType) {
		this.mappedProperty = mappedProperty;
		this.javaType = javaType;
		this.columnName = columnName;
		this.jdbcType = jdbcType;
		this.tableDefinition = tableDefinition;
	}

	public String getColumnName() {
		return columnName;
	}

	public JdbcType getJdbcType() {
		return jdbcType;
	}

	public boolean isUnsigned() {
		return unsigned;
	}

	public void setUnsigned(boolean unsigned) {
		this.unsigned = unsigned;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public TableDefinition getTableDefinition() {
		return tableDefinition;
	}

	public String getMappedProperty() {
		return mappedProperty;
	}

	public Type getJavaType() {
		return javaType;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public String toString() {
		return ToStringBuilder.reflectInvokeToString(this, PropertyFilters.newExcludedPropertyFilter(new String[] { "tableDefinition" }));
	}

}
