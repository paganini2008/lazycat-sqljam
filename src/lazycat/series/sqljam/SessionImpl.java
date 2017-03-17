package lazycat.series.sqljam;

import java.io.Serializable;
import java.util.List;

import lazycat.series.beans.PropertyUtils;
import lazycat.series.cache.Cache;
import lazycat.series.cache.LruCache;
import lazycat.series.jdbc.JdbcType;
import lazycat.series.jdbc.mapper.RowMapper;
import lazycat.series.sqljam.expression.Fields;
import lazycat.series.sqljam.expression.Identifier;
import lazycat.series.sqljam.expression.SqlParameterExpression;
import lazycat.series.sqljam.query.Query;
import lazycat.series.sqljam.transcation.Transaction;
import lazycat.series.sqljam.update.CascadeDelete;
import lazycat.series.sqljam.update.Delete;
import lazycat.series.sqljam.update.DeleteImpl;
import lazycat.series.sqljam.update.Insert;
import lazycat.series.sqljam.update.InsertImpl;
import lazycat.series.sqljam.update.Update;
import lazycat.series.sqljam.update.UpdateImpl;

/**
 * SessionImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SessionImpl implements Session {

	public SessionImpl(Transaction transaction, SessionExecutor sessionExecutor) {
		this.transaction = transaction;
		this.sessionExecutor = sessionExecutor;
		this.queryCache = new LruCache(sessionExecutor.getConfiguration().getQueryCacheSize());
	}

	private final Transaction transaction;
	private final SessionExecutor sessionExecutor;
	private final Cache queryCache;

	public void commit() {
		transaction.commit();
	}

	public void rollback() {
		transaction.rollback();
	}

	public int save(final Object object) {
		KeyStore keyStore = newKeyStore(object.getClass());
		int effected = insert(object.getClass()).values(object).execute(keyStore);
		if (effected > 0) {
			for (String keyProperty : keyStore.getKeyNames()) {
				PropertyUtils.setProperty(object, keyProperty, keyStore.getKey(keyProperty), sessionExecutor.getTypeConverter());
			}
		}
		return effected;
	}

	public int update(Object object) {
		return update(object.getClass(), null).set(object).filter(Identifier.create(object)).execute();
	}

	public <T> T get(Serializable id, Class<T> mappedClass) {
		return query(mappedClass).filter(new SqlParameterExpression(Fields.ID, id)).first();
	}

	public int delete(Object object) {
		return delete(object, false);
	}

	public int delete(Object object, boolean cascade) {
		return delete(object.getClass(), "this", cascade).filter(Identifier.create(object)).execute();
	}

	public <T> T getResult(Executable query, Class<T> requiredType) {
		return sessionExecutor.getResult(transaction, query, requiredType);
	}

	public <T> T getResult(String sql, Object[] parameters, JdbcType[] jdbcTypes, Class<T> requiredType) {
		return sessionExecutor.getResult(transaction, sql, parameters, jdbcTypes, requiredType);
	}

	public <T> T first(Executable query, Class<T> objectClass) {
		return sessionExecutor.first(transaction, query, objectClass);
	}

	public <T> List<T> list(Executable query, Class<T> objectClass) {
		return sessionExecutor.list(transaction, query, objectClass);
	}

	public <T> List<T> list(Executable query, RowMapper<T> rowMapper) {
		return sessionExecutor.list(transaction, query, rowMapper);
	}

	public int batch(Executable executable) {
		return sessionExecutor.batch(transaction, executable);
	}

	public int execute(Executable executable) {
		return sessionExecutor.update(transaction, executable);
	}

	public int execute(Executable executable, KeyStore keyStore) {
		return sessionExecutor.update(transaction, executable, keyStore);
	}

	public Insert insert(Class<?> mappedClass) {
		return new InsertImpl(this, mappedClass);
	}

	public Update update(Class<?> mappedClass) {
		return new UpdateImpl(this, mappedClass);
	}

	public Update update(Class<?> mappedClass, String tableAlias) {
		return new UpdateImpl(this, mappedClass, tableAlias);
	}

	public Delete delete(Class<?> mappedClass) {
		return delete(mappedClass, "this");
	}

	public Delete delete(Class<?> mappedClass, String tableAlias) {
		return delete(mappedClass, tableAlias, false);
	}

	public Delete delete(Class<?> mappedClass, String tableAlias, boolean cascade) {
		return cascade ? new CascadeDelete(this, mappedClass, tableAlias) : new DeleteImpl(this, mappedClass, tableAlias);
	}

	public Query query(Class<?> mappedClass, String tableAlias) {
		return getConfiguration().getFeature().createQueryExecutor(this, mappedClass, tableAlias);
	}

	public Query query(Class<?> mappedClass) {
		return query(mappedClass, "this");
	}

	public Query query(Query source, String tableAlias) {
		return getConfiguration().getFeature().createQueryExecutor(this, source, tableAlias);
	}

	public Query namedQuery(String name) {
		return (Query) queryCache.getObject(name);
	}

	public void close() {
		transaction.close();
	}

	public Configuration getConfiguration() {
		return sessionExecutor.getConfiguration();
	}

	public KeyStore newKeyStore(Class<?> mappedClass) {
		return sessionExecutor.newKeyStore(mappedClass);
	}

	public int executeSql(String sql, Object[] arguments) {
		return sessionExecutor.update(transaction, sql, arguments);
	}

	public void addCache(String name, Query query) {
		queryCache.putObject(name, query);
	}

	public SessionExecutor getSessionExecutor() {
		return sessionExecutor;
	}

}
