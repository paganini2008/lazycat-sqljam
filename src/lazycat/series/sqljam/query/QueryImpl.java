package lazycat.series.sqljam.query;

import java.util.Iterator;
import java.util.List;

import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.JoinType;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.expression.Expressions;
import lazycat.series.sqljam.expression.RelationShip;
import lazycat.series.sqljam.field.AbstractField;
import lazycat.series.sqljam.field.Asc;
import lazycat.series.sqljam.field.Columns;
import lazycat.series.sqljam.field.Desc;
import lazycat.series.sqljam.field.Field;
import lazycat.series.sqljam.field.FieldList;
import lazycat.series.sqljam.field.Functions;
import lazycat.series.sqljam.field.Select;
import lazycat.series.sqljam.field.StandardColumn;
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
		this.mappedClass = source.rootClass();
		this.queryable = new QueryableImpl(this, session);
		this.session = session;
	}

	public QueryImpl(Session session, Class<?> mappedClass, String tableAlias) {
		this.builder = new QueryBuilder(session, mappedClass, tableAlias);
		this.mappedClass = mappedClass;
		this.queryable = new QueryableImpl(this, session);
		this.session = session;
	}

	private final Queryable queryable;
	private LockMode lockMode;

	public Class<?> rootClass() {
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
		Expression where = builder.where;
		if (where != null) {
			builder.where = Expressions.and(where, expression);
		} else {
			builder.where = expression;
		}
		return this;
	}

	public Query relate(Class<?> mappedClass, String tableAlias) {
		return filter(new RelationShip(mappedClass, tableAlias));
	}

	public Queryable setTimeout(long timeout) {
		return queryable.setTimeout(timeout);
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

	public Query group(String property) {
		return group(new StandardColumn(property));
	}

	public Query group(Field field) {
		Field group = builder.group;
		if (group != null && field instanceof AbstractField) {
			group = ((AbstractField) group).sibling(field);
		} else {
			builder.group = field;
		}
		return this;
	}

	public Query having(Expression expression) {
		builder.having = expression;
		return this;
	}

	public Query asc(String property) {
		return order(new Asc(property));
	}

	public Query desc(String property) {
		return order(new Desc(property));
	}

	public Query order(Field field) {
		Field order = builder.order;
		if (order != null && field instanceof AbstractField) {
			order = ((AbstractField) order).sibling(field);
		} else {
			order = field;
		}
		return this;
	}

	public Query column(String property) {
		return column(property, property);
	}

	public Query column(String property, String alias) {
		return column(Columns.forName(property).as(alias));
	}

	public Query column(Field field) {
		Field columns = builder.columns;
		if (columns != null && columns instanceof AbstractField) {
			((AbstractField) columns).sibling(field);
		} else {
			builder.columns = field;
		}
		return this;
	}

	public Query columns(String... propertyNames) {
		return column(FieldList.columnList(propertyNames));
	}

	public Query max(String property) {
		return max(property, property);
	}

	public Query max(String property, String label) {
		return column(Functions.max(property).as(label));
	}

	public Query min(String property) {
		return min(property, property);
	}

	public Query min(String property, String label) {
		return column(Functions.min(property).as(label));
	}

	public Query avg(String property) {
		return avg(property, property);
	}

	public Query avg(String property, String label) {
		return column(Functions.avg(property).as(label));
	}

	public Query sum(String property) {
		return sum(property, property);
	}

	public Query sum(String property, String label) {
		return column(Functions.sum(property).as(label));
	}

	public Query distinct(String property) {
		return distinct(property, property);
	}

	public Query distinct(String property, String label) {
		return column(Columns.distinct(property).as(label));
	}

	public Query count(String property, String label) {
		return column(Functions.count(property).as(label));
	}

	public Query rows(String label) {
		return column(builder.getTableAlias(), label);
	}

	public Query rows(String tableAlias, String label) {
		return column(Functions.rows(tableAlias).as(label));
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
		return new Pageable(this, session, offset, limit);
	}

	public <T> T first() {
		return queryable.first();
	}

	public <T> T first(Class<T> beanClass) {
		return queryable.first(beanClass);
	}

	public <T> List<T> list() {
		return queryable.list();
	}

	public <T> List<T> list(Class<T> beanClass) {
		return queryable.list(beanClass);
	}

	public <T> Iterator<T> iterator() {
		return queryable.iterator();
	}

	public <T> Iterator<T> iterator(Class<T> beanClass) {
		return queryable.iterator(beanClass);
	}

	public <T> T one(Class<T> requiredType) {
		return queryable.one(requiredType);
	}

	public int into(Class<?> mappedClass) {
		return new InsertImpl(session, mappedClass).values(this, null).execute();
	}

	public int createAs(String tableName) {
		return new CreateAs(session, this, tableName).execute();
	}

	public int rows() {
		Query query = session.query(this, defaultTableAlias()).column(Functions.rows());
		return session.getResult(query, Integer.class);
	}

	public Query cache(String name) {
		session.cache(name, this);
		return this;
	}

	public Query selectAll() {
		return column(Select.ALL);
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

	public Class<?> findMappedClass(String tableAlias, Configuration metaData) {
		return builder.findMappedClass(tableAlias, metaData);
	}

	public Cacheable cacheAs(String name) {
		return new CacheableImpl(this, session, name);
	}

}
