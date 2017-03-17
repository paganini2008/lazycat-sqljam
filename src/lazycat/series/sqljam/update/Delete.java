package lazycat.series.sqljam.update;

import lazycat.series.sqljam.expression.Expression;

/**
 * Delete
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Delete extends Executor {

	Delete self();

	Delete filter(Expression expression);

	Delete crossJoin(Class<?> mappedClass, String tableAlias, Expression on);

	Delete innerJoin(Class<?> mappedClass, String tableAlias, Expression on);

	Delete leftJoin(Class<?> mappedClass, String tableAlias, Expression on);

	Delete rightJoin(Class<?> mappedClass, String tableAlias, Expression on);

	Delete fullJoin(Class<?> mappedClass, String tableAlias, Expression on);

}