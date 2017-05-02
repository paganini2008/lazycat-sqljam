package lazycat.series.sqljam;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import lazycat.series.jdbc.TransactionIsolationLevel;

/**
 * ProductMetadataService
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ProductMetadataService implements ProductMetadata {

	private String databaseProductName;
	private String databaseProductVersion;
	private int databaseMinorVersion;
	private int databaseMajorVersion;
	private String driverName;
	private String driverVersion;
	private int driverMinorVersion;
	private int driverMajorVersion;
	private TransactionIsolationLevel transactionIsolationLevel;

	public ProductMetadataService(ConnectionProvider connectionProvider) {
		Connection connection = connectionProvider.openConnectionImplicitly();
		try {
			DatabaseMetaData dbmd = connection.getMetaData();
			this.databaseProductName = dbmd.getDatabaseProductName();
			this.databaseProductVersion = dbmd.getDatabaseProductVersion();
			this.databaseMinorVersion = dbmd.getDatabaseMinorVersion();
			this.databaseMajorVersion = dbmd.getDatabaseMajorVersion();
			this.driverName = dbmd.getDriverName();
			this.driverVersion = dbmd.getDriverVersion();
			this.driverMinorVersion = dbmd.getDriverMinorVersion();
			this.driverMajorVersion = dbmd.getDriverMajorVersion();
			this.transactionIsolationLevel = TransactionIsolationLevel.find(dbmd.getDefaultTransactionIsolation());
		} catch (SQLException e) {
			throw new JdbcException(e);
		} finally {
			connectionProvider.closeConnection(connection);
		}
	}

	public String getDatabaseProductName() {
		return databaseProductName;
	}

	public String getDatabaseProductVersion() {
		return databaseProductVersion;
	}

	public int getDatabaseMinorVersion() {
		return databaseMinorVersion;
	}

	public int getDatabaseMajorVersion() {
		return databaseMajorVersion;
	}

	public String getDriverName() {
		return driverName;
	}

	public String getDriverVersion() {
		return driverVersion;
	}

	public int getDriverMinorVersion() {
		return driverMinorVersion;
	}

	public int getDriverMajorVersion() {
		return driverMajorVersion;
	}

	public TransactionIsolationLevel getDefaultTransactionIsolationLevel() {
		return transactionIsolationLevel;
	}

	public String toString() {
		return String.format(
				"DatabaseProductName=%s, DatabaseProductVersion=%s, DatabaseMajorVersion=%s, DatabaseMinorVersion=%s, DriverName=%s, DriverVersion=%s, DriverMajorVersion=%s, DriverMinorVersion=%s, DefaultTransactionIsolationLevel=%s",
				databaseProductName, databaseProductVersion, databaseMajorVersion, databaseMinorVersion, driverName,
				driverVersion, driverMajorVersion, driverMinorVersion, transactionIsolationLevel);
	}

}
