package lazycat.series.sqljam.generator;

import java.util.Date;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;

/**
 * NowGenerator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class NowGenerator implements Generator {

	public static final String NAME = "now";

	public boolean hasValue(Session session, Configuration configuration) {
		return true;
	}

	public Object postValue(Session session, Configuration configuration) {
		return new Date();
	}



}
