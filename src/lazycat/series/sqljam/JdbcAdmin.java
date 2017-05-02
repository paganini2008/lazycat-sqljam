package lazycat.series.sqljam;

import lazycat.series.sqljam.feature.Feature;

/**
 * JdbcAdmin
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface JdbcAdmin {

	Configuration getConfiguration();

	void setFeatureFactory(FeatureFactory featureFactory);

	void setFeature(Feature feature);

	Feature getFeature();

	boolean tableExists(Class<?> mappedClass);

	void truncateTable(Class<?> mappedClass);

	void exportTable(Class<?> mappedClass, String path);

	void alterTable(Class<?> mappedClass);

	void createTable(Class<?> mappedClass);

	void dropTable(Class<?> mappedClass);

	void validateTable(Class<?> mappedClass);
	
	void resolve(Class<?> mappedClass);

	void resolve();

	ConnectionProvider getConnectionProvider();

	SessionFactory buildSessionEngine();

	void releaseConnections();

}