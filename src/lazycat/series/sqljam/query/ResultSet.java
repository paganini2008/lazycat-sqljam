package lazycat.series.sqljam.query;

import java.util.List;

import lazycat.series.sqljam.Executable;

/**
 * ResultSet
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface ResultSet extends Executable {

	ResultSet lock();

	ResultSet lock(int timeout);

	<T> T getResult(Class<T> requiredType);

	<T> T first();

	<T> T first(Class<T> beanClass);

	<T> List<T> list();

	<T> List<T> list(Class<T> beanClass);

	int into(Class<?> mappedClass);

	int createAs(String tableName);
}
