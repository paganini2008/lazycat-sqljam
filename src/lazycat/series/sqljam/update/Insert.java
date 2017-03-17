package lazycat.series.sqljam.update;

import java.util.List;

import lazycat.series.sqljam.KeyStore;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.query.From;

/**
 * Insert
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Insert extends Executor {

	Insert values(Object object);

	Insert values(Object object, String[] excludeProperties);

	Insert values(List<?> objectList);

	Insert values(List<?> objectList, String[] excludeProperties);

	Insert values(From query, String[] includeProperties);

	Insert values(Expression expression);

	Batch batch(int flushSize);

	int batch(Batch batch);

	int execute(KeyStore keyStore);

}