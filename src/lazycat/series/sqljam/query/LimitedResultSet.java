package lazycat.series.sqljam.query;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;

/**
 * LimitedResultSet
 * 
 * @author Fred Feng
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class LimitedResultSet implements ResultSet {

	private final ResultSet delegate;
	private final Session session;
	private final Class<?> defaultMappedClass;

	public LimitedResultSet(ResultSet delegate, Session session, Class<?> defaultMappedClass) {
		this.delegate = delegate;
		this.session = session;
		this.defaultMappedClass = defaultMappedClass;
		this.timeout = session.getSessionFactory().getConfiguration().getDefaultSessionTimeout();
	}

	public String getText(Configuration configuration) {
		return delegate.getText(configuration);
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		delegate.setParameters(parameterCollector, configuration);
	}

	private int timeout;
	
	public ResultSet as(String name) {
		return delegate.as(name);
	}

	public ResultSet setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	public <T> T getResult(Class<T> requiredType, T defaultValue) {
		return doGetResultBackground(requiredType, defaultValue);
	}

	private <T> T doGetResultBackground(final Class<T> requiredType, T defaultValue) {
		return session.getSessionFactory().getThreadPool().submit(new Callable<T>() {
			public T call() throws Exception {
				return session.getResult(delegate, requiredType);
			}
		}, timeout, TimeUnit.SECONDS, defaultValue);
	}

	public <T> T first() {
		return (T) doFirstBackground(defaultMappedClass);
	}

	public <T> T first(Class<T> beanClass) {
		return doFirstBackground(beanClass);
	}

	private <T> T doFirstBackground(final Class<T> beanClass) {
		return session.getSessionFactory().getThreadPool().submit(new Callable<T>() {
			public T call() throws Exception {
				return session.first(delegate, beanClass);
			}
		}, timeout, TimeUnit.SECONDS, null);
	}

	public <T> List<T> list() {
		return (List<T>) doListBackground(defaultMappedClass);
	}

	public <T> List<T> list(Class<T> beanClass) {
		return doListBackground(beanClass);
	}

	private <T> List<T> doListBackground(final Class<T> beanClass) {
		return session.getSessionFactory().getThreadPool().submit(new Callable<List<T>>() {
			public List<T> call() throws Exception {
				return session.list(delegate, beanClass);
			}
		}, timeout, TimeUnit.SECONDS, null);
	}

	public <T> Iterator<T> iterator() {
		return (Iterator<T>) doIteratorBackground(defaultMappedClass);
	}

	public <T> Iterator<T> iterator(Class<T> beanClass) {
		return doIteratorBackground(beanClass);
	}

	private <T> Iterator<T> doIteratorBackground(final Class<T> beanClass) {
		return session.getSessionFactory().getThreadPool().submit(new Callable<Iterator<T>>() {
			public Iterator<T> call() throws Exception {
				return session.iterator(delegate, beanClass);
			}
		}, timeout, TimeUnit.SECONDS, null);
	}

	public int into(Class<?> mappedClass) {
		return delegate.into(mappedClass);
	}

	public int createAs(String tableName) {
		return delegate.createAs(tableName);
	}

	public ResultSet lock() {
		return delegate.lock();
	}

	public ResultSet lock(int timeout) {
		return delegate.lock(timeout);
	}

}
