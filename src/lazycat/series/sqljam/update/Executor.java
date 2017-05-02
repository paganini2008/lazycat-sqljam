package lazycat.series.sqljam.update;

import lazycat.series.sqljam.Executable;

/**
 * Executor
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Executor extends Executable {

	int execute();

	void executeAsync();

}
