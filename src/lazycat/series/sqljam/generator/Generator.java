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

	boolean hasValue(Session session, Configuration configuration);

	Object postValue(Session session, Configuration configuration);

}
