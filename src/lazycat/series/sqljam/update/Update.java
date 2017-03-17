package lazycat.series.sqljam.update;

import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.query.From;

/**
 * Update
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Update extends Executor {

	Update filter(Expression expression);

	Update set(Object object);

	Update set(String propertyName, Object parameter);

	Update set(String propertyName, From query);

	Update set(String propertyName, String anotherPropertyName);

	Update set(Expression expression);

}