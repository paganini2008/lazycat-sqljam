package lazycat.series.sqljam;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import lazycat.series.converter.TypeConverter;
import lazycat.series.jdbc.JdbcType;
import lazycat.series.jdbc.TypeHandlerRegistry;
import lazycat.series.jdbc.TypeHandlerRegistryImpl;
import lazycat.series.jdbc.mapper.RowMapper;
import lazycat.series.jdbc.type.TypeHandler;
import lazycat.series.sqljam.relational.ColumnDefinition;

public class ScalarRowMapper<T> implements RowMapper<T> {

	private final ColumnDefinition columnDefinition;
	private final Class<T> requiredType;
	private final TypeConverter typeConverter;

	public ScalarRowMapper(ColumnDefinition columnDefinition, Class<T> requiredType, TypeConverter typeConverter) {
		this.columnDefinition = columnDefinition;
		this.requiredType = requiredType;
		this.typeConverter = typeConverter;
	}

	public T mapRow(long rowIndex, ResultSet rs, TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
		Object result = getColumnValue(rs, columnDefinition.getColumnName(), columnDefinition.getJavaType(),
				columnDefinition.getJdbcType(), typeHandlerRegistry);
		Object converted = typeConverter != null ? typeConverter.convert(result, requiredType, null) : result;
		return requiredType.cast(converted);
	}

	protected Object getColumnValue(ResultSet rs, String columnName, Type javaType, JdbcType jdbcType,
			TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
		TypeHandler typeHandler = typeHandlerRegistry != null ? typeHandlerRegistry.getTypeHandler(javaType, jdbcType)
				: TypeHandlerRegistryImpl.getDefault();
		try {
			return typeHandler.getValue(rs, columnName);
		} catch (SQLException e) {
			return lookupColumnNameAndGetValue(rs, columnName, javaType, jdbcType, typeHandlerRegistry);
		}

	}

	private Object lookupColumnNameAndGetValue(ResultSet rs, String columnName, Type javaType, JdbcType jdbcType,
			TypeHandlerRegistry typeHandlerRegistry) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			if (columnDefinition.getMappedProperty().equals(rsmd.getColumnLabel(i))) {
				TypeHandler typeHandler = typeHandlerRegistry != null
						? typeHandlerRegistry.getTypeHandler(javaType, jdbcType) : TypeHandlerRegistryImpl.getDefault();
				return typeHandler.getValue(rs, i);
			}
		}
		throw new SQLException("Unknown columnName: " + columnName);
	}

}
