package lazycat.series.sqljam.query;

import java.util.Iterator;
import java.util.List;

import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.JoinType;
import lazycat.series.sqljam.MetaData;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.expression.Alias;
import lazycat.series.sqljam.expression.All;
import lazycat.series.sqljam.expression.Asc;
import lazycat.series.sqljam.expression.Column;
import lazycat.series.sqljam.expression.Desc;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.expression.ExpressionList;
import lazycat.series.sqljam.expression.Fields;
import lazycat.series.sqljam.expression.RowCount;
import lazycat.series.sqljam.update.CreateAs;
import lazycat.series.sqljam.update.InsertImpl;

/**
 * Select Clause Implementation
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class QueryImpl extends AbstractQuery implements Query {

	private final Session session;
	private final QueryBuilder builder;
	private final Class<?> mappedClass;

	public QueryImpl(Session session, Query source, String tableAlias) {
		this.builder = new QueryBuilder(session, source, tableAlias);
		this.mappedClass = source.defaultMappedClass();
		this.resultSet = new LimitedResultSet(this, session, mappedClass);
		this.session = session;
	}

	public QueryImpl(Session session, Class<?> mappedClass, String tableAlias) {
		this.builder = new QueryBuilder(session, mappedClass, tableAlias);
		this.mappedClass = mappedClass;
		this.resultSet = new LimitedResultSet(this, session, mappedClass);
		this.session = session;
	}

	private final ResultSet resultSet;
	private LockMode lockMode;

	public Class<?> defaultMappedClass() {
		return mappedClass;
	}

	public Query from(Class<?> mappedClass, String tableAlias) {
		builder.query = builder.query.join(mappedClass, tableAlias);
		return this;
	}

	public Query from(Query source, String tableAlias) {
		builder.query = builder.query.join(source, tableAlias);
		return this;
	}

	public Query filter(Expression expression) {
		builder.where = expression;
		return this;
	}

	public Query relation(Expression expression) {
		builder.relation = expression;
		return this;
	}

	public Query setTimeout(int timeout) {
		resultSet.setTimeout(timeout);
		return this;
	}

	public Query crossJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		return join(mappedClass, tableAlias, JoinType.CROSS_JOIN, on);
	}

	public Query crossJoin(Query source, String tableAlias, Expression on) {
		return join(source, tableAlias, JoinType.CROSS_JOIN, on);
	}

	public Query leftJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		return join(mappedClass, tableAlias, JoinType.LEFT_JOIN, on);
	}

	public Query leftJoin(Query source, String tableAlias, Expression on) {
		return join(source, tableAlias, JoinType.LEFT_JOIN, on);
	}

	public Query rightJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		return join(mappedClass, tableAlias, JoinType.RIGHT_JOIN, on);
	}

	public Query rightJoin(Query source, String tableAlias, Expression on) {
		return join(source, tableAlias, JoinType.RIGHT_JOIN, on);
	}

	public Query innerJoin(Query source, String tableAlias, Expression on) {
		return join(source, tableAlias, JoinType.INNER_JOIN, on);
	}

	public Query innerJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		return join(mappedClass, tableAlias, JoinType.INNER_JOIN, on);
	}

	public Query fullJoin(Query source, String tableAlias, Expression on) {
		return join(source, tableAlias, JoinType.FULL_JOIN, on);
	}

	public Query fullJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		return join(mappedClass, tableAlias, JoinType.FULL_JOIN, on);
	}

	private Query join(Class<?> mappedClass, String tableAlias, JoinType joinType, Expression on) {
		if (builder.join != null) {
			builder.join = builder.join.join(new SimpleFrom(mappedClass, tableAlias), joinType, on);
		} else {
			builder.join = new JoinDetail(new SimpleFrom(mappedClass, tableAlias), joinType, on, null);
		}
		return this;
	}

	private Query join(Query source, String tableAlias, JoinType joinType, Expression on) {
		if (builder.join != null) {
			builder.join = builder.join.join(new View(source, tableAlias), joinType, on);
		} else {
			builder.join = new JoinDetail(new View(source, tableAlias), joinType, on, null);
		}
		return this;
	}

	public Query group(String propertyName) {
		return group(new Column(propertyName));
	}

	public Query group(Expression expression) {
		final Expression groupBy = builder.group;
		if (groupBy instanceof ExpressionList) {
			((ExpressionList) groupBy).addExpression(expression);
		} else {
			builder.group = (expression instanceof ExpressionList) ? expression : ExpressionList.create(expression);
		}
		return this;
	}

	public Query having(Expression expression) {
		builder.having = expression;
		return this;
	}

	public Query asc(String propertyName) {
		return order(new Asc(propertyName));
	}

	public Query desc(String propertyName) {
		return order(new Desc(propertyName));
	}

	public Query order(Expression expression) {
		final Expression order = builder.order;
		if (order instanceof ExpressionList) {
			((ExpressionList) order).addExpression(expression);
		} else {
			builder.order = (expression instanceof ExpressionList) ? expression : ExpressionList.create(expression);
		}
		return this;
	}

	public Query column(String propertyName) {
		return column(new Column(propertyName));
	}

	public Query column(String propertyName, String alias) {
		return column(new Column(propertyName), alias);
	}

	public Query column(Expression expression) {
		final Expression field = builder.field;
		if (field instanceof ExpressionList) {
			((ExpressionList) field).addExpression(expression);
		} else {
			builder.field = (expression instanceof ExpressionList) ? expression : ExpressionList.create(expression);
		}
		return this;
	}

	public Query column(Expression expression, String alias) {
		expression = new Alias(expression, alias);
		return column(expression);
	}

	public Query max(String propertyName, String alias) {
		return column(Fields.max(propertyName), alias);
	}

	public Query min(String propertyName, String alias) {
		return column(Fields.min(propertyName), alias);
	}

	public Query avg(String propertyName, String alias) {
		return column(Fields.avg(propertyName), alias);
	}

	public Query sum(String propertyName, String alias) {
		return column(Fields.sum(propertyName), alias);
	}

	public Query distinct(String propertyName, String alias) {
		return column(Fields.distinct(propertyName), alias);
	}

	public Query count(String propertyName, String alias) {
		return column(Fields.count(propertyName), alias);
	}

	public Query countDistinct(String propertyName, String alias) {
		return column(Fields.countDistinct(propertyName), alias);
	}

	public ResultSet lock() {
		return lock(-1);
	}

	public ResultSet lock(int timeout) {
		this.lockMode = LockMode.lock(timeout);
		return this;
	}

	public ResultSet limit(int limit) {
		return limit(0, limit);
	}

	public ResultSet limit(int offset, int limit) {
		return new Pageable(this, session, defaultMappedClass(), offset, limit);
	}

	public <T> T first() {
		return resultSet.first();
	}

	public <T> T first(Class<T> beanClass) {
		return resultSet.first(beanClass);
	}

	public <T> List<T> list() {
		return resultSet.list();
	}

	public <T> List<T> list(Class<T> beanClass) {
		return resultSet.list(beanClass);
	}

	public <T> Iterator<T> iterator() {
		return resultSet.iterator();
	}

	public <T> Iterator<T> iterator(Class<T> beanClass) {
		return resultSet.iterator(beanClass);
	}

	public <T> T getResult(Class<T> requiredType, T defaultValue) {
		return resultSet.getResult(requiredType, defaultValue);
	}

	public int into(Class<?> mappedClass) {
		return new InsertImpl(session, mappedClass).values(this, null).execute();
	}

	public int createAs(String tableName) {
		return new CreateAs(session, this, tableName).execute();
	}

	public int rows() {
		Query query = new QueryImpl(session, this, defaultTableAlias()).column(RowCount.ONE);
		return session.getResult(query, Integer.class);
	}

	public Query cache(String name) {
		//session.addCache(name, this);
		return this;
	}

	public Query all() {
		return column(new All());
	}

	public int size() {
		int size = builder.query.size();
		if (builder.join != null) {
			size += builder.join.size();
		}
		return size;
	}

	protected final String defaultTableAlias() {
		String tableAlias = builder.getTableAlias();
		if (StringUtils.isBlank(tableAlias)) {
			tableAlias = "this";
		}
		return tableAlias + "_" + (size() + 1);
	}

	public String getText(Configuration configuration) {
		String sql = builder.getText(configuration);
		if (lockMode != null) {
			sql += lockMode.getText(configuration);
		}
		return sql;
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		builder.setParameters(parameterCollector, configuration);
	}

	public Class<?> findMappedClass(String tableAlias, MetaData metaData) {
		return builder.findMappedClass(tableAlias, metaData);
	}

	public ResultSet as(String name) {
		return null;
	}

}
