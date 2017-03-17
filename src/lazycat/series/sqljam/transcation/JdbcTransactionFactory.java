package lazycat.series.sqljam.transcation;

import java.sql.Connection;

import lazycat.series.jdbc.TransactionIsolationLevel;
import lazycat.series.sqljam.ConnectionProvider;

/**
 * 
 * @author admin
 *
 */
public class JdbcTransactionFactory implements TransactionFactory {

	public Transaction newTransaction(ConnectionProvider connectionProvider, TransactionIsolationLevel level, boolean autoCommit) {
		return new JdbcTranscation(connectionProvider, level, autoCommit);
	}

	public Transaction newTransaction(Connection connection) {
		return new JdbcTranscation(connection);
	}

}
