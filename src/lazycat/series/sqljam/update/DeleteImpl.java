package lazycat.series.sqljam.update;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.JoinType;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.field.Table;
import lazycat.series.sqljam.field.TableName;
import lazycat.series.sqljam.query.JoinDetail;
import lazycat.series.sqljam.query.SimpleFrom;

/**
 * DeleteImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class DeleteImpl extends AbstractExecutor implements Delete {

	private final DeleteBuilder builder;

	public DeleteImpl(Session session, Class<?> mappedClass) {
		this(session, mappedClass, "this");
	}

	public DeleteImpl(Session session, Class<?> mappedClass, String tableAlias) {
		this(session, new DeleteBuilder(session, mappedClass, tableAlias));
	}

	DeleteImpl(Session session, DeleteBuilder builder) {
		super(session);
		this.builder = builder;
	}

	public Delete self(boolean show) {
		builder.self = show ? TableName.THIS : Table.THIS;
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

}
