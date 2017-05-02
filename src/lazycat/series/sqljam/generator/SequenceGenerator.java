package lazycat.series.sqljam.generator;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;

/**
 * SequenceGenerator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SequenceGenerator implements Generator {

	public static final String NAME = "sequence";

	private final String generator;

	public SequenceGenerator(String generator) {
		this.generator = generator;
	}

	public Object postValue(Session session, Configuration configuration) {
		return session.getResult(configuration.getJdbcAdmin().getFeature().selectNextval(generator), null, null, Integer.class);
	}

	public boolean hasValue(Session session, Configuration configuration) {
		Object result = session.getResult(configuration.getJdbcAdmin().getFeature().selectCurrval(generator), null, null, Integer.class);
		return result != null;
	}
}
