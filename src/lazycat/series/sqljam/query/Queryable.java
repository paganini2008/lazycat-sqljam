package lazycat.series.sqljam.query;

import java.util.Iterator;

import lazycat.series.sqljam.Executable;

public interface Queryable extends Cacheable, Executable {

	void setTimeout(long timeout);

	<T> Iterator<T> iterator();

	<T> Iterator<T> iterator(Class<T> beanClass);
}
