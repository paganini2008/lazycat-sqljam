package lazycat.series.sqljam.update;

import java.util.List;

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
public class InsertImpl extends AbstractExecutor implements Insert {

	private final InsertBuilder builder;

	public InsertImpl(Session session, Class<?> mappedClass) {
		this(session, new InsertBuilder(session, mappedClass));
	}

	InsertImpl(Session session, InsertBuilder builder) {
		super(session);
		this.builder = builder;
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

	public int execute(KeyStore keyStore) {
		return session.execute(this, keyStore);
	}

	public Batch batch() {
		return new BatchImpl(session, this);
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
