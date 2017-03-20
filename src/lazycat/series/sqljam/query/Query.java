package lazycat.series.sqljam.query;

import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.expression.Expression;

/**
 * Select Clause
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Query extends From, ResultSet {

	Class<?> getMappedClass();

	Query from(Class<?> mappedClass, String alias);

	Query from(Query subQuery, String alias);

	Query filter(Expression expression);

	Query crossJoin(Class<?> mappedClass, String alias, Expression on);

	Query crossJoin(Query subQuery, String alias, Expression on);

	Query leftJoin(Class<?> mappedClass, String alias, Expression on);

	Query leftJoin(Query subQuery, String alias, Expression on);

	Query rightJoin(Class<?> mappedClass, String alias, Expression on);

	Query rightJoin(Query subQuery, String alias, Expression on);

	Query innerJoin(Query subQuery, String alias, Expression on);

	Query innerJoin(Class<?> mappedClass, String alias, Expression on);

	Query fullJoin(Query subQuery, String alias, Expression on);

	Query fullJoin(Class<?> mappedClass, String alias, Expression on);

	Query column(String property);

	Query column(String property, String alias);

	Query column(Expression expression);

	Query column(Expression expression, String alias);

	Query group(String property);

	Query group(Expression expression);

	Query having(Expression expression);

	Query asc(String property);

	Query desc(String property);

	Query order(Expression expression);

	Query distinct(String property, String alias);

	Query max(String property, String alias);

	Query min(String property, String alias);

	Query avg(String property, String alias);

	Query sum(String property, String alias);

	Query count(String property, String alias);

	Query countDistinct(String property, String alias);

	Query cache(String name);

	Query all();

	ResultSet limit(int limit);

	ResultSet limit(int offset, int limit);

	int rows();

	Session getSession();

}