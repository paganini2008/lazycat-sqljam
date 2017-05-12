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

	private final String sequenceName;

	public SequenceGenerator(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public Object postValue(Session session, Configuration configuration) {
		return session.getResult(configuration.getJdbcAdmin().getFeature().selectNextval(sequenceName), null, null, Integer.class);
	}

	public boolean hasValue(Session session, Configuration configuration) {
		Object result = session.getResult(configuration.getJdbcAdmin().getFeature().selectCurrval(sequenceName), null, null, Integer.class);
		return result != null;
	}
}
