package lazycat.series.sqljam;

import lazycat.series.cache.Cache;
import lazycat.series.cache.LruCache;
import lazycat.series.cache.UncheckedExpiredCache;
import lazycat.series.sqljam.transcation.JdbcTransactionFactory;
import lazycat.series.sqljam.transcation.TransactionFactory;

/**
 * StandardSessionAdmin
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class StandardSessionAdmin implements SessionAdmin {

	private final ThreadLocal<Session> sessionLocal = new ThreadLocal<Session>();
	private final ConnectionProvider connectionProvider;
	private final SessionOptions sessionOptions;
	private final SessionExecutor sessionExecutor;
	private final SessionPool sessionPool;
	private final Cache queryCache;
	private final Cache queryResultCache;
	private TransactionFactory transactionFactory = new JdbcTransactionFactory();

	public StandardSessionAdmin(SessionOptions sessionOptions, Configuration configuration, ConnectionProvider connectionProvider) {
		this.sessionExecutor = new SessionExecutorImpl(configuration);
		this.sessionPool = new SessionPool(sessionOptions.getSessionThreads(), sessionOptions.getMaxSessionPermits(),
				sessionOptions.getDefaultSessionTimeout());
		this.queryCache = new LruCache(sessionOptions.getQueryCacheSize());
		this.queryResultCache = new UncheckedExpiredCache(new LruCache(sessionOptions.getQueryResultCacheSize(),
				sessionOptions.getQueryResultOverflowStore()));
		this.sessionOptions = sessionOptions;
		this.connectionProvider = connectionProvider;
	}
	public SessionPool getSessionPool() {
		return sessionPool;
	}

	public Cache getQueryCache() {
		return queryCache;
	}

	public Cache getQueryResultCache() {
		return queryResultCache;
	}

	public void releaseExternalResources() {
		queryCache.close();
		queryResultCache.close();
		sessionPool.close();
	}

	public void setTransactionFactory(TransactionFactory transactionFactory) {
		this.transactionFactory = transactionFactory;
	}

	public Session openSession() {
		Session session = sessionLocal.get();
		if (session == null) {
			sessionLocal.set(new SessionImpl(transactionFactory.newTransaction(connectionProvider, this), this));
			session = sessionLocal.get();
		}
		return session;
	}

	public void closeSession() {
		sessionLocal.remove();
	}

	public SessionOptions getSessionOptions() {
		return sessionOptions;
	}

	public SessionExecutor getSessionExecutor() {
		return sessionExecutor;
	}

}
