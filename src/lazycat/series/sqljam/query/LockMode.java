package lazycat.series.sqljam.query;

import lazycat.series.sqljam.Configuration;

/**
 * LockMode
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class LockMode {

	public static final LockMode WAIT = new LockMode(-1);
	public static final LockMode NO_WAIT = new LockMode(0);

	private final int timeout;

	LockMode(int timeout) {
		this.timeout = timeout;
	}

	public String getText(Configuration configuration) {
		return configuration.getJdbcAdmin().getFeature().forUpdate(timeout);
	}

	public static LockMode lock(int timeout) {
		if (timeout < 0) {
			return WAIT;
		} else if (timeout == 0) {
			return NO_WAIT;
		} else {
			return new LockMode(timeout);
		}
	}

}
