package lazycat.series.sqljam;

import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import javax.sql.DataSource;

import lazycat.series.cache.Cache;
import lazycat.series.cache.CheckedExpiredCache;
import lazycat.series.cache.LruCache;
import lazycat.series.concurrent.LimitedThreadPool;
import lazycat.series.concurrent.ThreadPool;
import lazycat.series.jdbc.DataSourceFactory;
import lazycat.series.jdbc.TransactionIsolationLevel;
import lazycat.series.logger.LoggerFactory;
import lazycat.series.logger.MyLogger;
import lazycat.series.sqljam.transcation.JdbcTransactionFactory;
import lazycat.series.sqljam.transcation.TransactionFactory;

/**
 * SessionEngine
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SessionEngine implements SessionFactory {

	private static final MyLogger logger = LoggerFactory.getLogger(SessionEngine.class);

	private final static ThreadLocal<Session> sessionLocal = new ThreadLocal<Session>();
	private final JdbcAdmin jdbcAdmin;
	private final SessionExecutor sessionExecutor;
	private final TransactionIsolationLevel transactionIsolationLevel;
	private final boolean autoCommit;
	private final ThreadPool threadPool;
	private Cache globalCache;
	private TransactionFactory transactionFactory = new JdbcTransactionFactory();

	public SessionEngine(String driverClassName, String url, String username, String password,
			TransactionIsolationLevel transactionIsolationLevel, boolean autoCommit,
			ConfigurationInitializer configurationInitializer) {
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
			logger.info("Use default feature '{}'", configuration.getFeature());
		}
		if (transactionIsolationLevel == null) {
			transactionIsolationLevel = jdbcAdmin.getDefaultTransactionIsolationLevel();
			logger.info("Use default transactionIsolationLevel '{}'", transactionIsolationLevel);
		}

		logger.info("Validate DDL ...");
		jdbcAdmin.resolve();
		final AnnotationMetadata metaData = (AnnotationMetadata) configuration.getMetaData();
		metaData.watch(new Observer() {
			public void update(Observable o, Object arg) {
				if (arg == null) {
					throw new IllegalStateException("TableAlias is not sure.");
				}
				Class<?> mappedClass = (Class<?>) arg;
				logger.info("Loading mappedClass '{}'", mappedClass);
				metaData.mapClass(mappedClass);
				jdbcAdmin.resolve(mappedClass);
			}
		});

		this.sessionExecutor = new SessionExecutorImpl(configuration);
		this.threadPool = new LimitedThreadPool("session", configuration.getSessionThreads())
				.setConcurrency(configuration.getSessionConcurrency());
		this.transactionIsolationLevel = transactionIsolationLevel;
		this.autoCommit = autoCommit;
		this.jdbcAdmin = jdbcAdmin;
		if (configuration.isUseGlobalQueryResultCache()) {
			this.globalCache = new CheckedExpiredCache(new LruCache(configuration.getGlobalQueryResultCacheSize()), 5);
		}
	}

	public void setTransactionFactory(TransactionFactory transactionFactory) {
		this.transactionFactory = transactionFactory;
	}

	public SessionExecutor getSessionExecutor() {
		return sessionExecutor;
	}

	public ThreadPool getThreadPool() {
		return threadPool;
	}

	public Configuration getConfiguration() {
		return jdbcAdmin.getConfiguration();
	}

	public Session openSession() {
		Session session = sessionLocal.get();
		if (session == null) {
			sessionLocal.set(new SessionImpl(
					transactionFactory.newTransaction(jdbcAdmin.getConnectionProvider(), transactionIsolationLevel, autoCommit),
					this));
			session = sessionLocal.get();
		}
		return session;
	}

	void dispose() {
		sessionLocal.remove();
	}

	public void destroy() {
		threadPool.shutdown();
		if (globalCache != null) {
			globalCache.close();
		}
		jdbcAdmin.closeConnections();
	}
}
