package lazycat.series.sqljam;

import lazycat.series.jdbc.TransactionIsolationLevel;

/**
 * ProductMetadata
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface ProductMetadata {

	String getDatabaseProductName();

	String getDatabaseProductVersion();

	int getDatabaseMinorVersion();

	int getDatabaseMajorVersion();

	public String getDriverName();

	public String getDriverVersion();

	public int getDriverMinorVersion();

	public int getDriverMajorVersion();

	TransactionIsolationLevel getDefaultTransactionIsolationLevel();
}
