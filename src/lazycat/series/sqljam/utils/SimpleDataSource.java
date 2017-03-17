package lazycat.series.sqljam.utils;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Semaphore;

import lazycat.series.jdbc.AbstractDataSource;
import lazycat.series.jdbc.DBUtils;

/**
 * Build a DataSource that contains unpooled connection.
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SimpleDataSource extends AbstractDataSource {

	private String driver;
	private String url;
	private String username;
	private String password;
	private Boolean autoCommit;
	private Integer defaultTransactionIsolationLevel;
	private Integer maxSize = 16;

	public SimpleDataSource() {
	}

	private Semaphore semaphore;

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	public Integer getMaxSize() {
		return maxSize;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriverClassName(String driver) throws SQLException {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return username;
	}

	public void setUser(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public int getDefaultTransactionIsolationLevel() {
		return defaultTransactionIsolationLevel;
	}

	public void setDefaultTransactionIsolationLevel(int defaultTransactionIsolationLevel) {
		this.defaultTransactionIsolationLevel = defaultTransactionIsolationLevel;
	}

	public PrintWriter getLogWriter() throws SQLException {
		return DriverManager.getLogWriter();
	}

	public void setLogWriter(PrintWriter logWriter) throws SQLException {
		DriverManager.setLogWriter(logWriter);
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		DriverManager.setLoginTimeout(seconds);
	}

	public int getLoginTimeout() throws SQLException {
		return DriverManager.getLoginTimeout();
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException(getClass().getName() + " is not a wrapper.");
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public Connection getConnection() throws SQLException {
		if (semaphore == null) {
			synchronized (this) {
				if (semaphore == null) {
					semaphore = new Semaphore(maxSize);
				}
			}
		}
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		Connection connection = DriverManager.getConnection(url, username, password);
		configureConnection(connection);
		return new ConnectionProxy(connection, semaphore).getProxyConnection();
	}

	protected void configureConnection(Connection connection) throws SQLException {
		if (autoCommit != null && connection.getAutoCommit() != autoCommit) {
			connection.setAutoCommit(autoCommit);
		}
		if (defaultTransactionIsolationLevel != null) {
			connection.setTransactionIsolation(defaultTransactionIsolationLevel);
		}
	}

	static class ConnectionProxy implements InvocationHandler {

		private static final String CLOSE = "close";
		private static final Class<?>[] IFACES = new Class<?>[] { Connection.class };

		ConnectionProxy(Connection realConnection, Semaphore semaphore) {
			this.realConnection = realConnection;
			this.semaphore = semaphore;
			this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), IFACES, this);
		}

		private final Connection realConnection;
		private final Connection proxyConnection;
		private final Semaphore semaphore;

		public Connection getRealConnection() {
			return realConnection;
		}

		public Connection getProxyConnection() {
			return proxyConnection;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();
			if (methodName.equals("equals")) {
				return (realConnection == args[0]);
			} else if (methodName.equals("hashCode")) {
				return System.identityHashCode(realConnection);
			} else if (methodName.equals("toString")) {
				return realConnection.toString();
			} else if (CLOSE.hashCode() == methodName.hashCode() && CLOSE.equals(methodName)) {
				DBUtils.closeQuietly(realConnection);
				semaphore.release();
				return null;
			} else {
				try {
					return method.invoke(realConnection, args);
				} catch (Throwable t) {
					throw ExceptionUtils.unwrapThrowable(t);
				}
			}
		}

	}

}
