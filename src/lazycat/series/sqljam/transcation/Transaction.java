package lazycat.series.sqljam.transcation;

import java.sql.Connection;

/**
 * Transaction
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Transaction {

	String getId();

	long getElapsed();

	Connection getConnection() throws TransactionFault;

	void commit() throws TransactionFault;

	void rollback() throws TransactionFault;

	void close() throws TransactionFault;

}
