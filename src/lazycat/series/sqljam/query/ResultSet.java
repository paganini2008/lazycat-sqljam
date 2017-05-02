package lazycat.series.sqljam.query;

/**
 * ResultSet
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface ResultSet extends Queryable {

	Queryable lock();

	Queryable lock(int timeout);
	
	Class<?> rootClass();

	int into(Class<?> mappedClass);

	int createAs(String tableName);

}
