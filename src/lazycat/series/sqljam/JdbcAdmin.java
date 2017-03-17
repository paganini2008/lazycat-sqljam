package lazycat.series.sqljam;

import lazycat.series.jdbc.TransactionIsolationLevel;
import lazycat.series.sqljam.feature.Feature;

/**
 * JdbcAdmin
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface JdbcAdmin {

	void registerDdlResolver(AutoDdl autoDdl, DdlResolver ddlResolver);

	boolean tableExists(Class<?> mappedClass);

	void updateTable(Class<?> mappedClass);

	void createTable(Class<?> mappedClass);

	void dropTable(Class<?> mappedClass);

	void validateTable(Class<?> mappedClass);

	void resolve(Class<?> mappedClass);

	void resolve();

	ConnectionProvider getConnectionProvider();

	Configuration getConfiguration();

	TransactionIsolationLevel getDefaultTransactionIsolationLevel();

	String getDatabaseProductName();

	String getDatabaseProductVersion();

	int getDatabaseMinorVersion();

	int getDatabaseMajorVersion();

	public String getDriverName();

	public String getDriverVersion();

	public int getDriverMinorVersion();

	public int getDriverMajorVersion();

	Feature getDefaultFeature();

	SessionFactory buildSessionEngine(TransactionIsolationLevel transactionIsolationLevel, boolean autoCommit);

}