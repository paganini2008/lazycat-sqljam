package lazycat.series.sqljam.generator;

import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.feature.Feature;

/**
 * SequenceGenerator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SequenceGenerator implements Generator {

	private final String generator;

	public SequenceGenerator(String generator) {
		this.generator = generator;
	}

	public Object getValue(Feature feature, Session session) {
		return null;
	}

	public boolean hasValue(Feature feature, Session session) {
		return false;
	}

	public String getText(Feature feature, Session session) {
		return feature.nextval(generator);
	}

}
