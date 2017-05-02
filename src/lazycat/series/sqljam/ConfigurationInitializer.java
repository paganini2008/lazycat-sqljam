package lazycat.series.sqljam;

/**
 * ConfigurationInitializer
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface ConfigurationInitializer {

	void configure(Configuration configuration, SessionOptions sessionOptions);

}
