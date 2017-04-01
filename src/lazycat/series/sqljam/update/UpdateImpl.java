package lazycat.series.sqljam.update;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import lazycat.series.concurrent.FutureCallback;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.expression.ExpressionList;
import lazycat.series.sqljam.expression.Expressions;
import lazycat.series.sqljam.query.From;

/**
 * UpdateImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class UpdateImpl implements Update {

	private final Session session;
	private final UpdateBuilder builder;

	public UpdateImpl(Session session, Class<?> mappedClass) {
		this(session, mappedClass, "this");
	}

	public UpdateImpl(Session session, Class<?> mappedClass, String tableAlias) {
		this(session, new UpdateBuilder(session, mappedClass, tableAlias));
	}

	protected UpdateImpl(Session session, UpdateBuilder builder) {
		this.session = session;
		this.builder = builder;
	}

	public Update filter(Expression expression) {
		builder.where = expression;
		return this;
	}

	public Update set(Object object) {
		return set(new Setter(object));
	}

	public Update set(String property, String anotherPropertyName) {
		return set(Expressions.eq(property, anotherPropertyName));
	}

	public Update set(String property, From query) {
		return set(Expressions.eq(property, query));
	}

	public Update set(String property, Object parameter) {
		return set(Expressions.eq(property, parameter));
	}

	public Update set(Expression expression) {
		final Expression set = builder.set;
		if (set instanceof ExpressionList) {
			((ExpressionList) set).addExpression(expression);
		} else {
			builder.set = (expression instanceof ExpressionList) ? expression : ExpressionList.create(expression);
		}
		return this;
	}

	public int execute() {
		Configuration configuration = session.getSessionFactory().getConfiguration();
		return session.getSessionFactory().getThreadPool().submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.execute(UpdateImpl.this);
			}
		}, configuration.getDefaultSessionTimeout(), TimeUnit.SECONDS, 0);
	}

	public void execute(FutureCallback<Integer> callback) {
		Configuration configuration = session.getSessionFactory().getConfiguration();
		session.getSessionFactory().getThreadPool().submit(new ExecutorCallable() {
			public Integer call() throws Exception {
				return session.execute(UpdateImpl.this);
			}
		}, configuration.getDefaultSessionTimeout(), TimeUnit.SECONDS, callback);
	}

	public String getText(Configuration configuration) {
		return builder.getText(configuration);
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		builder.setParameters(parameterCollector, configuration);
	}

}
