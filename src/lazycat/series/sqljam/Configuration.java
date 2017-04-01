package lazycat.series.sqljam;

import lazycat.series.sqljam.feature.Feature;

/**
 * ORM framework Configuration
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Configuration {

	private final MetaData metaData = new AnnotationMetadata();
	private Feature feature;
	private boolean useGlobalQueryResultCache;
	private boolean useSessionQueryResultCache;
	private int globalQueryResultCacheSize = 1024;
	private int sessionQueryResultCacheSize = 512;
	private int queryCacheSize = 512;
	private int sessionThreads = Runtime.getRuntime().availableProcessors() * 2;
	private int sessionConcurrency;
	private int defaultSessionTimeout = 60;

	public Configuration() {
	}

	public int getDefaultSessionTimeout() {
		return defaultSessionTimeout;
	}

	public void setDefaultSessionTimeout(int defaultSessionTimeout) {
		this.defaultSessionTimeout = defaultSessionTimeout;
	}

	public int getSessionConcurrency() {
		return sessionConcurrency;
	}

	public int getSessionThreads() {
		return sessionThreads;
	}

	public Configuration setSessionThreads(int sessionThreads) {
		this.sessionThreads = sessionThreads;
		return this;
	}

	public Configuration setSessionConcurrency(int sessionConcurrency) {
		this.sessionConcurrency = sessionConcurrency;
		return this;
	}

	public Configuration scanPackage(String packageName) {
		metaData.scanPackage(packageName);
		return this;
	}

	public Configuration mapClass(Class<?> mappedClass) {
		metaData.mapClass(mappedClass);
		return this;
	}

	public MetaData getMetaData() {
		return metaData;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public int getQueryCacheSize() {
		return queryCacheSize;
	}

	public void setQueryCacheSize(int queryCacheSize) {
		this.queryCacheSize = queryCacheSize;
	}

	public int getGlobalQueryResultCacheSize() {
		return globalQueryResultCacheSize;
	}

	public void setGlobalQueryResultCacheSize(int globalQueryResultCacheSize) {
		this.globalQueryResultCacheSize = globalQueryResultCacheSize;
	}

	public int getSessionQueryResultCacheSize() {
		return sessionQueryResultCacheSize;
	}

	public void setSessionQueryResultCacheSize(int sessionQueryResultCacheSize) {
		this.sessionQueryResultCacheSize = sessionQueryResultCacheSize;
	}

	public boolean isUseGlobalQueryResultCache() {
		return useGlobalQueryResultCache;
	}

	public void setUseGlobalQueryResultCache(boolean useGlobalQueryResultCache) {
		this.useGlobalQueryResultCache = useGlobalQueryResultCache;
	}

	public boolean isUseSessionQueryResultCache() {
		return useSessionQueryResultCache;
	}

	public void setUseSessionQueryResultCache(boolean useSessionQueryResultCache) {
		this.useSessionQueryResultCache = useSessionQueryResultCache;
	}

}
