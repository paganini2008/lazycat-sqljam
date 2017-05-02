package lazycat.series.sqljam.transcation;

import java.sql.Connection;

/**
 * Transaction
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Transaction {

	long getId();

	long getElapsed();

	Connection getConnection() throws TransactionException;

	void commit() throws TransactionException;

	void rollback() throws TransactionException;

	void close() throws TransactionException;

}
