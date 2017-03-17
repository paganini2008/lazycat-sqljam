package lazycat.series.sqljam.generator;

import lazycat.series.logger.LazyLogger;
import lazycat.series.logger.LoggerFactory;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.feature.Feature;

/**
 * SequenceAssignedIdentifierGenerator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SequenceAssignedIdentifierGenerator implements IdentifierGenerator {

	private static final LazyLogger logger = LoggerFactory.getLogger(SequenceAssignedIdentifierGenerator.class);

	private final String generator;

	public SequenceAssignedIdentifierGenerator(String generator) {
		this.generator = generator;
	}

	public Object getValue(Feature feature, Session session) {
		return session.getResult(feature.selectNextval(generator), null, null, Integer.class);
	}

	public boolean hasValue(Feature feature, Session session) {
		if (feature.selectCurrvalIfNecessary()) {
			try {
				Integer result = session.getResult(feature.selectCurrval(generator), null, null, Integer.class);
				return result != null;
			} catch (RuntimeException e) {
				logger.error(e.getMessage(), e);
				return false;
			}
		}
		return true;
	}

	public String getText(Feature feature, Session session) {
		return "?";
	}
}
