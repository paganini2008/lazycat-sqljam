package lazycat.series.sqljam.query;

import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.expression.Field;

/**
 * Select Clause
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Query extends From, ResultSet {

	Query from(Class<?> mappedClass, String label);

	Query from(Query subQuery, String label);

	Query filter(Expression expression);

	Query crossJoin(Class<?> mappedClass, String label, Expression on);

	Query crossJoin(Query subQuery, String label, Expression on);

	Query leftJoin(Class<?> mappedClass, String label, Expression on);

	Query leftJoin(Query subQuery, String label, Expression on);

	Query rightJoin(Class<?> mappedClass, String label, Expression on);

	Query rightJoin(Query subQuery, String label, Expression on);

	Query innerJoin(Query subQuery, String label, Expression on);

	Query innerJoin(Class<?> mappedClass, String label, Expression on);

	Query fullJoin(Query subQuery, String label, Expression on);

	Query fullJoin(Class<?> mappedClass, String label, Expression on);

	Query relate(Class<?> mappedClass, String label);

	Query column(String property, String label);

	Query column(Field field);

	Query columns(String... properties);

	Query group(String property);

	Query group(Field field);

	Query having(Expression expression);

	Query asc(String property);

	Query desc(String property);

	Query order(Field field);

	Query distinct(String property, String label);

	Query max(String property, String label);

	Query min(String property, String label);

	Query avg(String property, String label);

	Query sum(String property, String label);

	Query count(String property, String label);

	Query countOne(String label);

	Query countAll(String label);

	Query countAll(String tableAlias, String label);

	Query cache(String name);

	Query selectAll();

	ResultSet limit(int limit);

	ResultSet limit(int offset, int limit);

	int rows();

}