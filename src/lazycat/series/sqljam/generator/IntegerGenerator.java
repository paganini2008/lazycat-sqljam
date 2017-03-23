package lazycat.series.sqljam.generator;

import lazycat.series.concurrent.AtomicPositiveInteger;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.feature.Feature;

/**
 * IncrementGenerationIdentifier
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class IntegerGenerator implements Generator {

	public static final String GENERATOR_NAME = "integer";

	private final AtomicPositiveInteger integer;
	private final int interval;

	public IntegerGenerator() {
		this(0, Integer.MAX_VALUE, 1);
	}

	public IntegerGenerator(int startValue, int maxValue, int interval) {
		this.integer = new AtomicPositiveInteger(startValue, maxValue);
		this.interval = interval;
	}

	public Object getValue(Feature feature, Session session) {
		return integer.getAndAdd(interval);
	}

	public String getText(Feature feature, Session session) {
		return "?";
	}

	public boolean hasValue(Feature feature, Session session) {
		integer.get();
		return true;
	}

	public boolean ignoreValue() {
		return false;
	}

}
