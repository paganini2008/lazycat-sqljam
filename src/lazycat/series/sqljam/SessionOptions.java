package lazycat.series.sqljam;

import lazycat.series.cache.OverflowStore;
import lazycat.series.converter.StandardTypeConverter;
import lazycat.series.converter.TypeConverter;
import lazycat.series.jdbc.TypeHandlerRegistry;
import lazycat.series.jdbc.TypeHandlerRegistryImpl;

/**
 * SessionOptions
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SessionOptions {

	private int sessionThreads = Runtime.getRuntime().availableProcessors() * 2;
	private int maxSessionPermits = 0;
	private int defaultSessionTimeout = 60;
	private int queryResultCacheSize = 512;
	private int queryCacheSize = 512;
	private OverflowStore queryResultOverflowStore;
	private TypeConverter typeConverter = new StandardTypeConverter();
	private TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistryImpl();

	public SessionOptions() {
	}

	public int getQueryResultCacheSize() {
		return queryResultCacheSize;
	}

	public SessionOptions setQueryResultCacheSize(int queryResultCacheSize) {
		this.queryResultCacheSize = queryResultCacheSize;
		return this;
	}

	public int getQueryCacheSize() {
		return queryCacheSize;
	}

	public SessionOptions setQueryCacheSize(int queryCacheSize) {
		this.queryCacheSize = queryCacheSize;
		return this;
	}

	public OverflowStore getQueryResultOverflowStore() {
		return queryResultOverflowStore;
	}

	public SessionOptions setQueryResultOverflowStore(OverflowStore queryResultOverflowStore) {
		this.queryResultOverflowStore = queryResultOverflowStore;
		return this;
	}

	public TypeConverter getTypeConverter() {
		return typeConverter;
	}

	public TypeHandlerRegistry getTypeHandlerRegistry() {
		return typeHandlerRegistry;
	}

	public int getMaxSessionPermits() {
		return maxSessionPermits;
	}

	public SessionOptions setMaxSessionPermits(int maxSessionPermits) {
		this.maxSessionPermits = maxSessionPermits;
		return this;
	}

	public int getSessionThreads() {
		return sessionThreads;
	}

	public SessionOptions setSessionThreads(int sessionThreads) {
		this.sessionThreads = sessionThreads;
		return this;
	}

	public int getDefaultSessionTimeout() {
		return defaultSessionTimeout;
	}

	public SessionOptions setDefaultSessionTimeout(int defaultSessionTimeout) {
		this.defaultSessionTimeout = defaultSessionTimeout;
		return this;
	}
}
