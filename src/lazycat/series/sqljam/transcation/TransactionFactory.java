package lazycat.series.sqljam.transcation;

import java.sql.Connection;

import lazycat.series.jdbc.TransactionIsolationLevel;
import lazycat.series.sqljam.ConnectionProvider;

public interface TransactionFactory {

	Transaction newTransaction(Connection connection);

	Transaction newTransaction(ConnectionProvider connectionProvider, TransactionIsolationLevel level, boolean autoCommit);

}
