package lazycat.series.sqljam.generator;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;

/**
 * CurrentTimestampGenerator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class CurrentTimestampGenerator implements Generator {

	public static final String CURRENT_TIMESTAMP = "current_timestamp";

	public boolean hasValue(Session session, Configuration configuration) {
		return true;
	}

	public Object postValue(Session session, Configuration configuration) {
		return System.currentTimeMillis();
	}

}
