package lazycat.series.sqljam;

/**
 * SessionFactory
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface SessionFactory {

	JdbcAdmin getJdbcAdmin();

	Session openSession();

	void destroy();

}