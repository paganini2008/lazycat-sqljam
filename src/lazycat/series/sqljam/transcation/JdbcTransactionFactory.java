package lazycat.series.sqljam.transcation;

import lazycat.series.jdbc.TransactionIsolationLevel;
import lazycat.series.sqljam.ConnectionProvider;
import lazycat.series.sqljam.SessionAdmin;

/**
 * JdbcTransactionFactory
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class JdbcTransactionFactory implements TransactionFactory {

	private TransactionIsolationLevel transactionIsolationLevel;
	private Boolean autoCommit;

	public void setTransactionIsolationLevel(TransactionIsolationLevel transactionIsolationLevel) {
		this.transactionIsolationLevel = transactionIsolationLevel;
	}

	public void setAutoCommit(Boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public Transaction newTransaction(ConnectionProvider connectionProvider, SessionAdmin sessionAdmin) {
		return new JdbcTranscation(connectionProvider, transactionIsolationLevel, autoCommit, sessionAdmin);
	}

}
