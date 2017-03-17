package lazycat.series.sqljam.update;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.JoinType;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.expression.TableAlias;
import lazycat.series.sqljam.query.JoinDetail;
import lazycat.series.sqljam.query.SimpleFrom;

/**
 * DeleteImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class DeleteImpl implements Delete {

	private final DeleteBuilder builder;
	private final Session session;

	public DeleteImpl(Session session, Class<?> mappedClass) {
		this(session, mappedClass, "this");
	}

	public DeleteImpl(Session session, Class<?> mappedClass, String tableAlias) {
		this(session, new DeleteBuilder(session, mappedClass, tableAlias));
	}

	protected DeleteImpl(Session session, DeleteBuilder builder) {
		this.builder = builder;
		this.session = session;
	}

	public Delete self() {
		builder.alias = new TableAlias();
		return this;
	}

	public Delete crossJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		return join(mappedClass, tableAlias, JoinType.CROSS_JOIN, on);
	}

	public Delete leftJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		return join(mappedClass, tableAlias, JoinType.LEFT_JOIN, on);
	}

	public Delete rightJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		return join(mappedClass, tableAlias, JoinType.RIGHT_JOIN, on);
	}

	public Delete innerJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		return join(mappedClass, tableAlias, JoinType.INNER_JOIN, on);
	}

	public Delete fullJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		return join(mappedClass, tableAlias, JoinType.FULL_JOIN, on);
	}

	private Delete join(Class<?> mappedClass, String tableAlias, JoinType joinType, Expression on) {
		if (builder.join != null) {
			builder.join = builder.join.join(new SimpleFrom(mappedClass, tableAlias), joinType, on);
		} else {
			builder.join = new JoinDetail(new SimpleFrom(mappedClass, tableAlias), joinType, on, null);
		}
		return this;
	}

	public Delete filter(Expression expression) {
		builder.where = expression;
		return this;
	}

	public String getText(Configuration configuration) {
		return builder.getText(configuration);
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		builder.setParameters(parameterCollector, configuration);
	}

	public int execute() {
		return session.execute(this);
	}

}
