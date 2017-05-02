package lazycat.series.sqljam;

/**
 * Query Context
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Context {

	Class<?> findMappedClass(String tableAlias, Configuration configuration);

	String getTableAlias();

}
