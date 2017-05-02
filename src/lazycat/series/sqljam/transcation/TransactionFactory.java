package lazycat.series.sqljam.transcation;

import lazycat.series.jdbc.TransactionIsolationLevel;
import lazycat.series.sqljam.ConnectionProvider;
import lazycat.series.sqljam.SessionAdmin;

/**
 * TransactionFactory
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface TransactionFactory {

	void setAutoCommit(Boolean autoCommit);

	void setTransactionIsolationLevel(TransactionIsolationLevel transactionIsolationLevel);

	Transaction newTransaction(ConnectionProvider connectionProvider, SessionAdmin sessionAdmin);

}
