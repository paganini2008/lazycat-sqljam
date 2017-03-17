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
	private boolean useQueryCache = false;
	private int queryResultCacheSize = 1024;
	private int queryCacheSize = 512;

	public Configuration() {
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

	public int getQueryResultCacheSize() {
		return queryResultCacheSize;
	}

	public void setQueryResultCacheSize(int queryResultCacheSize) {
		this.queryResultCacheSize = queryResultCacheSize;
	}

	public boolean isUseQueryCache() {
		return useQueryCache;
	}

	public void setUseQueryCache(boolean useQueryCache) {
		this.useQueryCache = useQueryCache;
	}

}
