package lazycat.series.sqljam.generator;

import java.util.UUID;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;

/**
 * GuidGenerator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class GuidGenerator implements Generator {

	public static final String NAME = "guid";

	public Object postValue(Session session, Configuration configuration) {
		return UUID.randomUUID().toString();
	}

	public boolean hasValue(Session session, Configuration configuration) {
		return true;
	}

}
