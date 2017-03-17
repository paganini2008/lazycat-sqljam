package lazycat.series.sqljam;

/**
 * DdlResolver
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface DdlResolver {

	void resolve(JdbcAdmin jdbcAdmin, Class<?> mappedClass);

}
