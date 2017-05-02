package lazycat.series.sqljam;

import lazycat.series.cache.Cache;
import lazycat.series.sqljam.transcation.TransactionFactory;

/**
 * SessionBuilder
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface SessionAdmin {

	void setTransactionFactory(TransactionFactory transactionFactory);

	SessionOptions getSessionOptions();

	SessionExecutor getSessionExecutor();

	Cache getQueryCache();

	Cache getQueryResultCache();

	SessionPool getSessionPool();

	void releaseExternalResources();

	Session openSession();

	void closeSession();

}