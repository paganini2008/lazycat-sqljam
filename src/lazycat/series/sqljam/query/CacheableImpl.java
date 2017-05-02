package lazycat.series.sqljam.query;

import java.util.List;

import lazycat.series.cache.Cache;
import lazycat.series.sqljam.Session;

@SuppressWarnings("unchecked")
public class CacheableImpl implements Cacheable {

	private final Cacheable delegate;
	private final Session session;
	private final String name;

	public CacheableImpl(Cacheable delegate, Session session, String name) {
		this.delegate = delegate;
		this.session = session;
		this.name = name;
	}

	public <T> T one(Class<T> requiredType) {
		Cache cache = session.getSessionAdmin().getQueryResultCache();
		Object result = cache.getObject(name);
		if (result == null) {
			cache.putObject(name, delegate.one(requiredType));
			result = cache.getObject(name);
		}
		return (T) result;
	}

	public <T> T first() {
		Cache cache = session.getSessionAdmin().getQueryResultCache();
		Object result = cache.getObject(name);
		if (result == null) {
			cache.putObject(name, delegate.first());
			result = cache.getObject(name);
		}
		return (T) result;
	}

	public <T> T first(Class<T> beanClass) {
		Cache cache = session.getSessionAdmin().getQueryResultCache();
		Object result = cache.getObject(name);
		if (result == null) {
			cache.putObject(name, delegate.first(beanClass));
			result = cache.getObject(name);
		}
		return (T) result;
	}

	public <T> List<T> list() {
		Cache cache = session.getSessionAdmin().getQueryResultCache();
		Object result = cache.getObject(name);
		if (result == null) {
			cache.putObject(name, delegate.list());
			result = cache.getObject(name);
		}
		return (List<T>) result;
	}

	public <T> List<T> list(Class<T> beanClass) {
		Cache cache = session.getSessionAdmin().getQueryResultCache();
		Object result = cache.getObject(name);
		if (result == null) {
			cache.putObject(name, delegate.list(beanClass));
			result = cache.getObject(name);
		}
		return (List<T>) result;
	}

}
