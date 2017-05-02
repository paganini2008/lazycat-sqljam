package lazycat.series.sqljam.generator;

import lazycat.series.concurrent.AtomicPositiveInteger;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;

/**
 * IdentityGenerator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class IdentityGenerator implements Generator {

	public static final String NAME = "identity";

	private final AtomicPositiveInteger integer;
	private final int interval;

	public IdentityGenerator() {
		this(0, Integer.MAX_VALUE, 1);
	}

	public IdentityGenerator(int startValue, int maxValue, int interval) {
		this.integer = new AtomicPositiveInteger(startValue, maxValue);
		this.interval = interval;
	}

	public boolean hasValue(Session session, Configuration configuration) {
		return true;
	}

	public Object postValue(Session session, Configuration configuration) {
		return integer.getAndAdd(interval);
	}

}
