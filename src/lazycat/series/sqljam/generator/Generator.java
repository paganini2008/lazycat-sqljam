package lazycat.series.sqljam.generator;

import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.feature.Feature;

/**
 * Generator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Generator {

	String getText(Feature feature, Session session);

	boolean hasValue(Feature feature, Session session);

	Object getValue(Feature feature, Session session);

}
