package lazycat.series.sqljam;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import javax.sql.DataSource;

import lazycat.series.jdbc.DataSourceFactory;
import lazycat.series.jdbc.TransactionIsolationLevel;
import lazycat.series.logger.LazyLogger;
import lazycat.series.logger.LoggerFactory;
import lazycat.series.sqljam.transcation.JdbcTransactionFactory;
import lazycat.series.sqljam.transcation.TransactionFactory;
import lazycat.series.sqljam.update.Executor;

/**
 * SessionEngine
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SessionEngine implements SessionFactory {

	private static final LazyLogger logger = LoggerFactory.getLogger(SessionEngine.class);

	private final static ThreadLocal<Session> sessionLocal = new ThreadLocal<Session>();
	private final JdbcAdmin jdbcAdmin;
	private final SessionExecutor sessionExecutor;
	private final TransactionIsolationLevel transactionIsolationLevel;
	private final boolean autoCommit;
	private TransactionFactory transactionFactory = new JdbcTransactionFactory();

	public SessionEngine(String driverClassName, String url, String username, String password,
			TransactionIsolationLevel transactionIsolationLevel, boolean autoCommit, ConfigurationInitializer configurationInitializer) {
		this(new StandardJdbcAdmin(driverClassName, url, username, password), transactionIsolationLevel, autoCommit,
				configurationInitializer);
	}

	public SessionEngine(DataSource dataSource, TransactionIsolationLevel transactionIsolationLevel, boolean autoCommit,
			ConfigurationInitializer configurationInitializer) {
		this(new StandardJdbcAdmin(dataSource), transactionIsolationLevel, autoCommit, configurationInitializer);
	}

	public SessionEngine(DataSourceFactory factory, TransactionIsolationLevel transactionIsolationLevel, boolean autoCommit,
			ConfigurationInitializer configurationInitializer) throws SQLException {
		this(new StandardJdbcAdmin(factory), transactionIsolationLevel, autoCommit, configurationInitializer);
	}

	protected SessionEngine(final JdbcAdmin jdbcAdmin, TransactionIsolationLevel transactionIsolationLevel, boolean autoCommit,
			ConfigurationInitializer configurationInitializer) {
		Configuration configuration = jdbcAdmin.getConfiguration();
		if (configurationInitializer != null) {
			configurationInitializer.configure(configuration);
		}

		if (configuration.getFeature() == null) {
			configuration.setFeature(jdbcAdmin.getDefaultFeature());
			logger.info("Undefine feature and instead default setting '?'", configuration.getFeature());
		}
		if (transactionIsolationLevel == null) {
			transactionIsolationLevel = jdbcAdmin.getDefaultTransactionIsolationLevel();
			logger.info("Undefine transactionIsolationLevel and instead default setting '?'", transactionIsolationLevel);
		}

		logger.info("Validate DDL ...");
		jdbcAdmin.resolve();

		final AnnotationMetadata metaData = (AnnotationMetadata) configuration.getMetaData();
		metaData.watch(new Observer() {

			public void update(Observable o, Object arg) {
				if (arg == null) {
					throw new IllegalStateException("Alias is not sure.");
				}
				Class<?> mappedClass = (Class<?>) arg;
				logger.info("Delay load mappedClass '?'", mappedClass);
				metaData.mapClass(mappedClass);
				jdbcAdmin.resolve(mappedClass);
			}
		});

		this.sessionExecutor = new SessionExecutorImpl(configuration);

		this.transactionIsolationLevel = transactionIsolationLevel;
		this.autoCommit = autoCommit;
		this.jdbcAdmin = jdbcAdmin;
	}

	public void setTransactionFactory(TransactionFactory transactionFactory) {
		this.transactionFactory = transactionFactory;
	}

	public SessionExecutor getSessionExecutor() {
		return sessionExecutor;
	}

	public Session openSession(boolean autoRollback) {
		return new RollbackableSession(openSession()).getProxySession();
	}

	public Session openSession() {
		Session session = sessionLocal.get();
		if (session == null) {
			sessionLocal.set(new SessionImpl(
					transactionFactory.newTransaction(jdbcAdmin.getConnectionProvider(), transactionIsolationLevel, autoCommit),
					sessionExecutor));
			session = sessionLocal.get();
		}
		return session;
	}

	private static class RollbackableSession implements InvocationHandler {

		private final Session realSession;
		private final Session proxySession;

		private RollbackableSession(Session session) {
			this.realSession = session;
			this.proxySession = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class<?>[] { Session.class },
					this);
		}

		Session getProxySession() {
			return proxySession;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();
			if (methodName.equals("execute") && args[0] instanceof Executor) {
				try {
					return method.invoke(realSession, args);
				} catch (RuntimeException e) {
					realSession.rollback();
					throw e;
				}
			}
			return method.invoke(realSession, args);
		}

	}
}
