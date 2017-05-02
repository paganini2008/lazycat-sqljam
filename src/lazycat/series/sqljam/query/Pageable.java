package lazycat.series.sqljam.query;

import java.util.Iterator;
import java.util.List;

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
public class Pageable implements ResultSet, CacheAdapter {

	private final ResultSet delegate;
	private final Session session;
	private final int offset;
	private final int limit;
	private LockMode lockMode;
	private final Queryable queryable;

	public Pageable(ResultSet delegate, Session session, int offset, int limit) {
		this.delegate = delegate;
		this.session = session;
		this.offset = offset;
		this.limit = limit;
		this.queryable = new QueryableImpl(this, session);
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
			sql.append(configuration.getJdbcAdmin().getFeature().getPageableSqlString(delegate.getText(configuration), offset, limit));
		}
		if (lockMode != null) {
			sql.append(lockMode.getText(configuration));
		}
		return sql.toString();
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		delegate.setParameters(parameterCollector, configuration);
	}

	public void setTimeout(long timeout) {
		queryable.setTimeout(timeout);
	}

	public <T> T one(Class<T> requiredType) {
		return queryable.one(requiredType);
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

	public int into(Class<?> mappedClass) {
		return session.insert(mappedClass).values(this, null).execute();
	}

	public int createAs(String tableName) {
		return new CreateAs(session, this, tableName).execute();
	}

	public Queryable lock() {
		return lock(-1);
	}

	public Queryable lock(int timeout) {
		this.lockMode = LockMode.lock(timeout);
		return this;
	}

	public Cacheable cacheAs(String name) {
		return new CacheableImpl(this, session, name);
	}

	public Class<?> rootClass() {
		return delegate.rootClass();
	}

}
