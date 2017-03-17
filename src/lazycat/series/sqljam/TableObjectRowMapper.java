package lazycat.series.sqljam;

import java.lang.reflect.Type;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import lazycat.series.beans.PropertyUtils;
import lazycat.series.jdbc.JdbcType;
import lazycat.series.jdbc.mapper.BasicRowMapper;
import lazycat.series.jdbc.mapper.RowMapperException;
import lazycat.series.reflect.ConstructorUtils;
import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * TableObjectRowMapper
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class TableObjectRowMapper<T> extends BasicRowMapper<T> {

	public TableObjectRowMapper(TableDefinition tableDefinition) {
		this.tableDefinition = tableDefinition;
	}

	private final TableDefinition tableDefinition;

	protected Type getJavaType(ResultSetMetaData rsmd, int columnIndex) {
		String columnDisplayName;
		try {
			columnDisplayName = getColumnDisplayName(rsmd, columnIndex);
		} catch (SQLException e) {
			throw new JdbcFault(e);
		}
		ColumnDefinition cd = tableDefinition.getColumn(columnDisplayName);
		return cd != null ? cd.getJavaType() : null;
	}

	protected JdbcType getJdbcType(ResultSetMetaData rsmd, int columnIndex) {
		String columnDisplayName;
		try {
			columnDisplayName = getColumnDisplayName(rsmd, columnIndex);
		} catch (SQLException e) {
			throw new JdbcFault(e);
		}
		ColumnDefinition cd = tableDefinition.getColumn(columnDisplayName);
		return cd != null ? cd.getJdbcType() : null;
	}

	@SuppressWarnings("unchecked")
	protected T createObject(int columnCount) {
		try {
			return (T) ConstructorUtils.invokeConstructor(tableDefinition.getMappedClass(), null);
		} catch (Exception e) {
			throw new RowMapperException(e);
		}
	}

	protected void setValue(T object, String columnName, int columnIndex, Object columnValue) {
		PropertyUtils.setProperty(object, columnName, columnValue, null);
	}

}
