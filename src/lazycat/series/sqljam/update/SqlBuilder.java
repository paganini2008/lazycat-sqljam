package lazycat.series.sqljam.update;

import lazycat.series.sqljam.Context;
import lazycat.series.sqljam.Executable;

/**
 * SqlBuilder
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface SqlBuilder extends Executable, Context{
	
	SqlBuilder copy();
	
}


