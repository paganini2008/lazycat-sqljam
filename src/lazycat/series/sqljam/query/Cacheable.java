package lazycat.series.sqljam.query;

import java.util.List;

/**
 * Cacheable
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Cacheable {

	<T> T one(Class<T> requiredType);

	<T> T first();

	<T> T first(Class<T> beanClass);

	<T> List<T> list();

	<T> List<T> list(Class<T> beanClass);

}
