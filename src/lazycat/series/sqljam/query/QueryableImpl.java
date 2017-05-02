package lazycat.series.sqljam.query;

import java.util.Iterator;
import java.util.List;

import lazycat.series.concurrent.Call;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;

/**
 * QueryableImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class QueryableImpl implements Queryable {

	private final ResultSet delegate;
	private final Session session;

	public QueryableImpl(ResultSet delegate, Session session) {
		this.delegate = delegate;
		this.session = session;
	}

	public String getText(Configuration configuration) {
		return delegate.getText(configuration);
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		delegate.setParameters(parameterCollector, configuration);
	}

	private long timeout;

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public <T> T one(final Class<T> requiredType) {
		return (T) session.getSessionAdmin().getSessionPool().execute(new Call() {
			public Object call() throws Exception {
				return session.getResult(delegate, requiredType);
			}
		}, timeout);
	}

	public <T> T first() {
		return (T) session.getSessionAdmin().getSessionPool().execute(new Call() {
			public Object call() throws Exception {
				return session.first(delegate, delegate.rootClass());
			}
		}, timeout);
	}

	public <T> T first(final Class<T> beanClass) {
		return (T) session.getSessionAdmin().getSessionPool().execute(new Call() {
			public Object call() throws Exception {
				return session.first(delegate, beanClass);
			}
		}, timeout);
	}

	public <T> List<T> list() {
		return (List<T>) session.getSessionAdmin().getSessionPool().execute(new Call() {
			public Object call() throws Exception {
				return delegate.list();
			}
		}, timeout);
	}

	public <T> List<T> list(final Class<T> beanClass) {
		return (List<T>) session.getSessionAdmin().getSessionPool().execute(new Call() {
			public Object call() throws Exception {
				return session.list(delegate, beanClass);
			}
		}, timeout);
	}

	public <T> Iterator<T> iterator() {
		return (Iterator<T>) session.getSessionAdmin().getSessionPool().execute(new Call() {
			public Object call() throws Exception {
				return session.iterator(delegate);
			}
		}, timeout);
	}

	public <T> Iterator<T> iterator(final Class<T> beanClass) {
		return (Iterator<T>) session.getSessionAdmin().getSessionPool().execute(new Call() {
			public Object call() throws Exception {
				return session.iterator(delegate, beanClass);
			}
		}, timeout);
	}

}
