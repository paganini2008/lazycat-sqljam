package lazycat.series.sqljam.generator;

import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.feature.Feature;

/**
 * CurrentTimeGenerator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class CurrentTimeGenerator implements Generator {

	public String getText(Feature feature, Session session) {
		return "?";
	}

	public boolean hasValue(Feature feature, Session session) {
		return true;
	}

	public Object getValue(Feature feature, Session session) {
		return System.currentTimeMillis();
	}

}
