package lazycat.series.sqljam.update;

import lazycat.series.concurrent.AtomicPositiveInteger;
import lazycat.series.concurrent.NamedCallable;

/**
 * ExecutorCallable
 * 
 * @author Fred Feng
 * @version 1.0
 */
public abstract class ExecutorCallable implements NamedCallable<Integer> {

	private final static AtomicPositiveInteger integer = new AtomicPositiveInteger();

	public String getName() {
		return "Executor-" + integer.incrementAndGet();
	}

}
