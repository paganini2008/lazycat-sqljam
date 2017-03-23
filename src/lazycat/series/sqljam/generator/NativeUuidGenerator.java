package lazycat.series.sqljam.generator;

import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.feature.Feature;

/**
 * NativeUuidIdentifierGenerator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class NativeUuidGenerator implements Generator {
	
	public static final String GENERATOR_NAME = "native_uuid";

	public Object getValue(Feature feature, Session session) {
		return session.getResult(feature.selectUUID(), null, null, String.class);
	}

	public String getText(Feature feature, Session session) {
		return "?";
	}

	public boolean hasValue(Feature feature, Session session) {
		return true;
	}
	
	public boolean ignoreValue() {
		return false;
	}

}
