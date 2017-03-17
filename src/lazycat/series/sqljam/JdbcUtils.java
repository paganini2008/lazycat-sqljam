package lazycat.series.sqljam;

import static lazycat.series.jdbc.DBUtils.toIterator;
import static lazycat.series.jdbc.DBUtils.toMap;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import lazycat.series.jdbc.DBUtils;
import lazycat.series.jdbc.JdbcType;

/**
 * JdbcUtils
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class JdbcUtils {

	private JdbcUtils() {
	}

	public static Iterator<Map<String, Object>> foreignKeyMetadataIterator(DatabaseMetaData dbmd, String catalog, String schema,
			String tableName) throws SQLException {
		ResultSet rs = null;
		try {
			rs = dbmd.getExportedKeys(catalog, schema, tableName);
			return rs != null ? toIterator(rs, true) : null;
		} finally {
			DBUtils.closeQuietly(rs);
		}
	}

	public static Iterator<Map<String, Object>> primaryKeyMetadataIterator(DatabaseMetaData dbmd, String catalog, String schema,
			String tableName) throws SQLException {
		ResultSet rs = null;
		try {
			rs = dbmd.getPrimaryKeys(catalog, schema, tableName);
			return rs != null ? toIterator(rs, true) : null;
		} finally {
			DBUtils.closeQuietly(rs);
		}
	}

	public static Map<String, Object> getColumnMetadata(DatabaseMetaData dbmd, String catalog, String schema, String tableName,
			String fieldName) throws SQLException {
		ResultSet rs = null;
		try {
			rs = dbmd.getColumns(null, schema, tableName, fieldName);
			return (rs != null && rs.next()) ? toMap(rs, true) : null;
		} finally {
			DBUtils.closeQuietly(rs);
		}
	}

	public static Iterator<Map<String, Object>> uniqueKeyMetadataIterator(DatabaseMetaData dbmd, String catalog, String schema,
			String tableName) throws SQLException {
		ResultSet rs = null;
		try {
			rs = dbmd.getIndexInfo(catalog, schema, tableName, true, false);
			return rs != null ? toIterator(rs, true) : null;
		} finally {
			DBUtils.closeQuietly(rs);
		}
	}

	public static Map<String, Object> getTableMetadata(DatabaseMetaData dbmd, String catalog, String schema, String tableName)
			throws SQLException {
		ResultSet rs = null;
		try {
			rs = dbmd.getTables(catalog, schema, tableName, new String[] { "TABLE" });
			return (rs != null && rs.next()) ? toMap(rs,true) : null;
		} finally {
			DBUtils.closeQuietly(rs);
		}
	}

	public static boolean uniqueKeyExists(DatabaseMetaData dbmd, String catalog, String schema, String tableName,
			String indexName) throws SQLException {
		Iterator<Map<String, Object>> iter = uniqueKeyMetadataIterator(dbmd, catalog, schema, tableName);
		Map<String, Object> m;
		while (iter.hasNext()) {
			m = iter.next();
			if (indexName.equals(m.get("INDEX_NAME"))) {
				return true;
			}
		}
		return false;
	}

	public static boolean primaryKeyExists(DatabaseMetaData dbmd, String catalog, String schema, String tableName,
			String indexName) throws SQLException {
		Iterator<Map<String, Object>> iter = primaryKeyMetadataIterator(dbmd, catalog, schema, tableName);
		Map<String, Object> m;
		while (iter.hasNext()) {
			m = iter.next();
			if (indexName.equals(m.get("INDEX_NAME"))) {
				return true;
			}
		}
		return false;
	}

	public static boolean foreignKeyExists(DatabaseMetaData dbmd, String catalog, String schema, String tableName, String fkName)
			throws SQLException {
		Iterator<Map<String, Object>> iter = foreignKeyMetadataIterator(dbmd, catalog, schema, tableName);
		Map<String, Object> m;
		while (iter.hasNext()) {
			m = iter.next();
			if (fkName.equals(m.get("FK_NAME"))) {
				return true;
			}
		}
		return false;
	}

	public static boolean columnExists(DatabaseMetaData dbmd, String catalog, String schema, String tableName, String columnName)
			throws SQLException {
		ResultSet rs = null;
		try {
			rs = dbmd.getColumns(catalog, schema, tableName, columnName);
			return (rs != null && rs.next()) ? columnName.equalsIgnoreCase(rs.getString("COLUMN_NAME")) : false;
		} finally {
			DBUtils.closeQuietly(rs);
		}
	}

	public static boolean tableExists(DatabaseMetaData dbmd, String catalog, String schema, String tableName)
			throws SQLException {
		ResultSet rs = null;
		try {
			rs = dbmd.getTables(catalog, schema, tableName, new String[] { "TABLE" });
			return (rs != null && rs.next()) ? tableName.equalsIgnoreCase(rs.getString("TABLE_NAME")) : false;
		} finally {
			DBUtils.closeQuietly(rs);
		}
	}

	public static boolean isAssigned(JdbcType fromType, JdbcType toType) {
		if (fromType == toType) {
			return true;
		}
		if (fromType == JdbcType.VARCHAR) {
			return toType == JdbcType.LONGVARCHAR;
		} else if (fromType == JdbcType.TINYINT) {
			return toType == JdbcType.SMALLINT || toType == JdbcType.INTEGER || toType == JdbcType.BIGINT;
		} else if (fromType == JdbcType.SMALLINT) {
			return toType == JdbcType.INTEGER || toType == JdbcType.BIGINT;
		} else if (fromType == JdbcType.INTEGER) {
			return toType == JdbcType.BIGINT;
		} else if (fromType == JdbcType.FLOAT) {
			return toType == JdbcType.REAL || toType == JdbcType.DOUBLE;
		} else if (fromType == JdbcType.REAL) {
			return toType == JdbcType.DOUBLE;
		}
		return false;
	}

	public static boolean isNumber(JdbcType jdbcType) {
		return isNumeric(jdbcType) || isInteger(jdbcType);
	}

	public static boolean isNumeric(JdbcType jdbcType) {
		return jdbcType == JdbcType.DECIMAL || jdbcType == JdbcType.NUMERIC || jdbcType == JdbcType.REAL
				|| jdbcType == JdbcType.DOUBLE || jdbcType == JdbcType.FLOAT;
	}

	public static boolean isInteger(JdbcType jdbcType) {
		return jdbcType == JdbcType.TINYINT || jdbcType == JdbcType.SMALLINT || jdbcType == JdbcType.INTEGER
				|| jdbcType == JdbcType.BIGINT;
	}

	public static void main(String[] args) throws Exception {
		String url = "jdbc:mysql://127.0.0.1:3306/demo";
		Connection connection = DriverManager.getConnection(url, "root", "12345678");
		Map<String, Object> map = getColumnMetadata(connection.getMetaData(), null, null, "tb_image", "id");
		System.out.println(((String) map.get("COLUMN_DEF")).length());
		// Map<String, Object> map = getJdbcInfo(connection.getMetaData());
		// for (Map.Entry<String, Object> e : map.entrySet()) {
		// System.out.println(e);
		// }
		System.out.println(JdbcType.find((Integer) map.get("SQL_DATA_TYPE")));
		System.out.println(JdbcType.find((Integer) map.get("DATA_TYPE")));

		boolean existed = primaryKeyExists(connection.getMetaData(), null, null, "tb_album", "id");
		System.out.println(existed);

		System.out.println("-------------------------------");
		Iterator<Map<String, Object>> iter = foreignKeyMetadataIterator(connection.getMetaData(), null, null, "tb_album");
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
		connection.close();
	}
}
