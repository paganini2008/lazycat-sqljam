package lazycat.series.sqljam.query;

import lazycat.series.sqljam.Context;
import lazycat.series.sqljam.Executable;

/**
 * From Clause
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface From extends Executable, Context {

	From join(Class<?> mappedClass, String tableAlias);

	From join(From otherQuery, String tableAlias);

	int size();

}
