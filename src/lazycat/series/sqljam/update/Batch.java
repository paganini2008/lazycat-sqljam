package lazycat.series.sqljam.update;

import lazycat.series.sqljam.Executable;

/**
 * Batch
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Batch extends Executor {

	Batch push(Object object);

	void flush();

	Batch setFlushSize(int flushSize);

}
