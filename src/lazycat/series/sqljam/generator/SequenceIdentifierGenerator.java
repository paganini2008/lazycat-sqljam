package lazycat.series.sqljam.generator;

import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.feature.Feature;

/**
 * SequenceIdentifierGenerator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SequenceIdentifierGenerator implements IdentifierGenerator {

	private final String generator;

	public SequenceIdentifierGenerator(String generator) {
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
