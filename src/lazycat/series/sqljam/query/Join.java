package lazycat.series.sqljam.query;

import lazycat.series.sqljam.Context;
import lazycat.series.sqljam.JoinType;
import lazycat.series.sqljam.expression.Expression;

/**
 * Join
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Join extends Expression, Context {

	Join join(From source, JoinType joinType, Expression on);

	int size();

}
