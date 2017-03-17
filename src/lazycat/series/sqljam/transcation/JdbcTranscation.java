package lazycat.series.sqljam.transcation;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import lazycat.series.jdbc.DBUtils;
import lazycat.series.jdbc.TransactionIsolationLevel;
import lazycat.series.sqljam.ConnectionProvider;

/**
 * JdbcTranscation
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class JdbcTranscation implements Transaction {

	public JdbcTranscation(ConnectionProvider connectionProvider, TransactionIsolationLevel level, boolean autoCommit) {
		this.transactionId = UUID.randomUUID().toString();
		this.connectionProvider = connectionProvider;
		this.level = level;
		this.autoCommit = autoCommit;
		this.expired = false;
		this.begin = System.currentTimeMillis();
	}

	public JdbcTranscation(Connection connection) {
		this.transactionId = UUID.randomUUID().toString();
		this.connection = connection;
		this.expired = false;
		this.begin = System.currentTimeMillis();
	}

	private final String transactionId;
	private final long begin;
	private long elapsed;
	private Connection connection;
	private ConnectionProvider connectionProvider;
	private TransactionIsolationLevel level;
	private boolean autoCommit;
	private boolean expired;

	public Connection getConnection() throws TransactionFault {
		checkExpired();
		if (connection == null) {
			try {
				openConnection();
			} catch (SQLException e) {
				throw new TransactionFault("Can't open database connection.", e);
			}
		}
		return connection;
	}

	public void commit() throws TransactionFault {
		checkExpired();
		try {
			if (connection != null && !connection.getAutoCommit()) {
				connection.commit();
			}
		} catch (SQLException e) {
			throw new TransactionFault(e);
		}
	}

	public void rollback() throws TransactionFault {
		checkExpired();
		try {
			if (connection != null && !connection.getAutoCommit()) {
				connection.rollback();
			}
		} catch (SQLException e) {
			throw new TransactionFault(e);
		}
	}

	public void close() throws TransactionFault {
		checkExpired();
		try {
			resetAutoCommit();
		} catch (SQLException e) {
			throw new TransactionFault(e);
		}
		if (connectionProvider != null) {
			connectionProvider.closeConnection(connection);
		} else {
			DBUtils.closeQuietly(connection);
		}
		this.elapsed = System.currentTimeMillis() - begin;
		this.expired = true;
	}

	private void checkExpired() {
		if (expired) {
			throw new TransactionFault("Transaction is expired.");
		}
	}

	protected void resetAutoCommit() throws SQLException {
		if (connection != null && !connection.getAutoCommit()) {
			connection.setAutoCommit(true);
		}
	}

	protected void openConnection() throws SQLException {
		connection = connectionProvider.openConnection();
		if (level != null) {
			connection.setTransactionIsolation(level.getLevel());
		}
		if (connection.getAutoCommit() != autoCommit) {
			connection.setAutoCommit(autoCommit);
		}
	}

	public long getElapsed() {
		return elapsed;
	}

	public String getId() {
		return transactionId;
	}

}
