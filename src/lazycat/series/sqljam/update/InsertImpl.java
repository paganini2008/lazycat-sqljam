package lazycat.series.sqljam.update;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import lazycat.series.concurrent.FutureCallback;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.KeyStore;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.query.From;

/**
 * InsertImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class InsertImpl implements Insert {

	private final Session session;
	private final InsertBuilder builder;

	public InsertImpl(Session session, Class<?> mappedClass) {
		this(session, new InsertBuilder(session, mappedClass));
	}

	protected InsertImpl(Session session, InsertBuilder builder) {
		this.builder = builder;
		this.session = session;
	}

	public Insert values(Object object) {
		return values(object, null);
	}

	public Insert values(Object object, String[] excludeProperties) {
		return values(new Values(object).excludeProperties(excludeProperties));
	}

	public Insert values(List<?> objectList) {
		return values(objectList, null);
	}

	public Insert values(List<?> objectList, String[] excludeProperties) {
		return values(new ValuesList(objectList).excludeProperties(excludeProperties));
	}

	public Insert values(From query, String[] includedProperties) {
		return values(new QueryValues(query).includedProperties(includedProperties));
	}

	public Insert values(Expression expression) {
		builder.values = expression;
		return this;
	}

	public int execute() {
		Configuration configuration = session.getSessionFactory().getConfiguration();
		return session.getSessionFactory().getThreadPool().submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.execute(InsertImpl.this);
			}
		}, configuration.getDefaultSessionTimeout(), TimeUnit.SECONDS, 0);
	}

	public void execute(FutureCallback<Integer> callback) {
		Configuration configuration = session.getSessionFactory().getConfiguration();
		session.getSessionFactory().getThreadPool().submit(new ExecutorCallable() {
			public Integer call() throws Exception {
				return session.execute(InsertImpl.this);
			}
		}, configuration.getDefaultSessionTimeout(), TimeUnit.SECONDS, callback);
	}

	public int execute(final KeyStore keyStore) {
		Configuration configuration = session.getSessionFactory().getConfiguration();
		return session.getSessionFactory().getThreadPool().submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.execute(InsertImpl.this, keyStore);
			}
		}, configuration.getDefaultSessionTimeout(), TimeUnit.SECONDS, 0);
	}

	public Batch batch() {
		return new BatchImpl(this, session);
	}

	public int batch(Batch batch) {
		return session.batch(batch);
	}

	public Session getSession() {
		return session;
	}

	public String getText(Configuration configuration) {
		return builder.getText(configuration);
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		builder.setParameters(parameterCollector, configuration);
	}

}
