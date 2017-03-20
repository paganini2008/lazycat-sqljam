package lazycat.series.sqljam.update;

import java.util.Iterator;
import java.util.List;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.query.LockMode;
import lazycat.series.sqljam.query.Query;
import lazycat.series.sqljam.query.ResultSet;

/**
 * Pageable
 * 
 * @author Fred Feng
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class Pageable implements ResultSet {

	private final Query query;
	private final int offset;
	private final int limit;
	private LockMode lockMode;

	public Pageable(Query query, int offset, int limit) {
		this.query = query;
		this.offset = offset;
		this.limit = limit;
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
			sql.append(configuration.getFeature().getPageableSqlString(query.getText(configuration), offset, limit));
		}
		if (lockMode != null) {
			sql.append(lockMode.getText(configuration));
		}
		return sql.toString();
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		query.setParameters(parameterCollector, configuration);
	}

	public <T> T getResult(Class<T> requiredType) {
		return query.getSession().getResult(this, requiredType);
	}

	public <T> T first() {
		return (T) query.getSession().first(this, query.getMappedClass());
	}

	public <T> T first(Class<T> beanClass) {
		return query.getSession().first(this, beanClass);
	}

	public <T> List<T> list() {
		return (List<T>) query.getSession().list(this, query.getMappedClass());
	}

	public <T> List<T> list(Class<T> beanClass) {
		return query.getSession().list(this, beanClass);
	}

	public <T> Iterator<T> iterator() {
		return (Iterator<T>) query.getSession().iterator(this, query.getMappedClass());
	}

	public <T> Iterator<T> iterator(Class<T> beanClass) {
		return query.getSession().iterator(this, beanClass);
	}

	public int into(Class<?> mappedClass) {
		return query.getSession().insert(mappedClass).values(this, null).execute();
	}

	public int createAs(String tableName) {
		return new CreateAs(query.getSession(), this, tableName).execute();
	}

	public ResultSet lock() {
		return lock(-1);
	}

	public ResultSet lock(int timeout) {
		this.lockMode = LockMode.lock(timeout);
		return this;
	}

}
