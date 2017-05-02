package lazycat.series.sqljam.generator;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;

/**
 * Generator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Generator {

	static final String INSTANCE = "global";

	boolean hasValue(Session session, Configuration configuration);

	Object postValue(Session session, Configuration configuration);

}
