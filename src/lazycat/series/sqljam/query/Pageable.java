package lazycat.series.sqljam.query;

import java.util.Iterator;
import java.util.List;

import lazycat.series.cache.Cache;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.update.CreateAs;

/**
 * Pageable
 * 
 * @author Fred Feng
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class Pageable implements ResultSet {

	private final ResultSet delegate;
	private final Session session;
	private final int offset;
	private final int limit;
	private LockMode lockMode;
	private String name;
	private final ResultSet real;

	public Pageable(ResultSet delegate, Session session, Class<?> defaultMappedClass, int offset, int limit) {
		this.delegate = delegate;
		this.session = session;
		this.offset = offset;
		this.limit = limit;
		this.real = new LimitedResultSet(this, session, defaultMappedClass);
	}

	public String getText(Configuration configuration) {
		if (offset < 0) {
			throw new IllegalArgumentException("Offset must be > 0.");
		}
		if (limit < offset) {
			throw new IllegalArgumentException("Limit must be > Offset.");
		}
		StringBuilder sql = new StringBuilder();
		if (limit > 0) {
			sql.append(configuration.getFeature().getPageableSqlString(delegate.getText(configuration), offset, limit));
		}
		if (lockMode != null) {
			sql.append(lockMode.getText(configuration));
		}
		return sql.toString();
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		delegate.setParameters(parameterCollector, configuration);
	}

	public ResultSet setTimeout(int timeout) {
		real.setTimeout(timeout);
		return this;
	}

	public <T> T getResult(Class<T> requiredType, T defaultValue) {
		Cache cache = session.getCache();
		if (cache == null) {
			return real.getResult(requiredType, defaultValue);
		}
		Object result = cache.getObject(name);
		if (result == null) {
			cache.putObject(name, real.getResult(requiredType, defaultValue));
			result = cache.getObject(name);
		}
		return (T) result;
	}

	public <T> T first() {
		return real.first();
	}

	public <T> T first(Class<T> beanClass) {
		return real.first(beanClass);
	}

	public <T> List<T> list() {
		return real.list();
	}

	public <T> List<T> list(Class<T> beanClass) {
		return real.list(beanClass);
	}

	public <T> Iterator<T> iterator() {
		return real.iterator();
	}

	public <T> Iterator<T> iterator(Class<T> beanClass) {
		return real.iterator(beanClass);
	}

	public int into(Class<?> mappedClass) {
		return session.insert(mappedClass).values(this, null).execute();
	}

	public int createAs(String tableName) {
		return new CreateAs(session, this, tableName).execute();
	}

	public ResultSet lock() {
		return lock(-1);
	}

	public ResultSet lock(int timeout) {
		this.lockMode = LockMode.lock(timeout);
		return this;
	}

	public ResultSet as(String name) {
		this.name = name;
		return this;
	}

}
