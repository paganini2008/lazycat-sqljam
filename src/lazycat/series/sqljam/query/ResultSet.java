package lazycat.series.sqljam.query;

import java.util.Iterator;
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

	ResultSet setTimeout(int timeout);

	ResultSet as(String name);

	<T> T getResult(Class<T> requiredType, T defaultValue);

	<T> T first();

	<T> T first(Class<T> beanClass);

	<T> List<T> list();

	<T> List<T> list(Class<T> beanClass);

	<T> Iterator<T> iterator();

	<T> Iterator<T> iterator(Class<T> beanClass);

	int into(Class<?> mappedClass);

	int createAs(String tableName);
}
