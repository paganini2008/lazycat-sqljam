package lazycat.series.sqljam.transcation;

import java.sql.Connection;
import java.sql.SQLException;

import lazycat.series.concurrent.AtomicPositiveLong;
import lazycat.series.jdbc.DBUtils;
import lazycat.series.jdbc.TransactionIsolationLevel;
import lazycat.series.sqljam.ConnectionProvider;
import lazycat.series.sqljam.SessionAdmin;

/**
 * JdbcTranscation
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class JdbcTranscation implements Transaction {

	private static final AtomicPositiveLong transactionIdGenerator = new AtomicPositiveLong(0);

	public JdbcTranscation(ConnectionProvider connectionProvider, TransactionIsolationLevel level, Boolean autoCommit,
			SessionAdmin sessionAdmin) {
		this.transactionId = transactionIdGenerator.incrementAndGet();
		this.connectionProvider = connectionProvider;
		this.level = level;
		this.autoCommit = autoCommit;
		this.expired = false;
		this.begin = System.currentTimeMillis();
		this.sessionAdmin = sessionAdmin;
	}

	private final SessionAdmin sessionAdmin;
	private final long transactionId;
	private final long begin;
	private long elapsed;
	private Connection connection;
	private ConnectionProvider connectionProvider;
	private TransactionIsolationLevel level;
	private Boolean autoCommit;
	private boolean expired;

	public Connection getConnection() throws TransactionException {
		checkExpired();
		if (connection == null) {
			try {
				openConnection();
			} catch (SQLException e) {
				throw new TransactionException("Can't open database connection.", e);
			}
		}
		return connection;
	}

	public void commit() throws TransactionException {
		checkExpired();
		try {
			if (connection != null && !connection.getAutoCommit()) {
				connection.commit();
			}
		} catch (SQLException e) {
			throw new TransactionException(e);
		}
	}

	public void rollback() throws TransactionException {
		checkExpired();
		try {
			if (connection != null && !connection.getAutoCommit()) {
				connection.rollback();
			}
		} catch (SQLException e) {
			throw new TransactionException(e);
		}
	}

	public void close() throws TransactionException {
		checkExpired();
		try {
			resetAutoCommit();
		} catch (SQLException e) {
			throw new TransactionException(e);
		}
		if (connectionProvider != null) {
			connectionProvider.closeConnection(connection);
		} else {
			DBUtils.closeQuietly(connection);
		}
		this.elapsed = System.currentTimeMillis() - begin;
		this.expired = true;

		sessionAdmin.closeSession();
	}

	private void checkExpired() {
		if (expired) {
			throw new TransactionException("Transaction is expired.");
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
		if (autoCommit != null && connection.getAutoCommit() != autoCommit) {
			connection.setAutoCommit(autoCommit);
		}
	}

	public long getElapsed() {
		return elapsed;
	}

	public long getId() {
		return transactionId;
	}

}
