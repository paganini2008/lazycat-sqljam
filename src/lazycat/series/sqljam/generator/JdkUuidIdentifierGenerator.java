package lazycat.series.sqljam.generator;

import java.util.UUID;

import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.feature.Feature;

/**
 * JdkUuidIdentifierGenerator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class JdkUuidIdentifierGenerator implements IdentifierGenerator {

	public static final String GENERATOR_NAME = "jdk_uuid";
	
	public String getText(Feature feature, Session session) {
		return "?";
	}

	public boolean hasValue(Feature feature, Session session) {
		return true;
	}

	public Object getValue(Feature feature, Session session) {
		return UUID.randomUUID().toString();
	}
	
	public boolean ignoreValue() {
		return false;
	}

}
