package lazycat.series.sqljam;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lazycat.series.beans.PropertyUtils;
import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.expression.Expressions;
import lazycat.series.sqljam.expression.Identifier;
import lazycat.series.sqljam.field.ID;
import lazycat.series.sqljam.query.Query;
import lazycat.series.sqljam.query.QueryImpl;
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

	public SessionImpl(Transaction transaction, SessionAdmin sessionAdmin) {
		this.transaction = transaction;
		this.sessionAdmin = sessionAdmin;
	}

	private final Transaction transaction;
	private final SessionAdmin sessionAdmin;

	public void commit() {
		transaction.commit();
	}

	public void rollback() {
		transaction.rollback();
	}

	public int save(final Object object) {
		KeyStore keyStore = keyStore(object.getClass());
		int effected = insert(object.getClass()).values(object).execute(keyStore);
		if (effected > 0) {
			for (String keyProperty : keyStore.getKeyNames()) {
				PropertyUtils.setProperty(object, keyProperty, keyStore.getKey(keyProperty),
						sessionAdmin.getSessionOptions().getTypeConverter());
			}
		}
		return effected;
	}

	public int update(Object object) {
		return update(object.getClass(), null).set(object).filter(Identifier.create(object)).execute();
	}

	public <T> T get(Serializable id, Class<T> mappedClass) {
		return query(mappedClass).filter(Expressions.eq(ID.THIS, id)).first();
	}

	public int delete(Object object) {
		return delete(object, false);
	}

	public int delete(Object object, boolean cascade) {
		return delete(object.getClass(), ROOT_ALIAS, cascade).filter(Identifier.create(object)).execute();
	}

	public <T> T getResult(Executable query, Class<T> requiredType) {
		return sessionAdmin.getSessionExecutor().getResult(transaction, query, requiredType);
	}

	public <T> T getResult(String sql, Object[] parameters, JdbcType[] jdbcTypes, Class<T> requiredType) {
		return sessionAdmin.getSessionExecutor().getResult(transaction, sql, parameters, jdbcTypes, requiredType);
	}

	public <T> T first(Executable query, Class<T> objectClass) {
		return sessionAdmin.getSessionExecutor().first(transaction, query, objectClass);
	}

	public <T> Iterator<T> iterator(Executable query, Class<T> beanClass) {
		return sessionAdmin.getSessionExecutor().iterator(transaction, query, beanClass);
	}

	public <T> Iterator<Map<String, Object>> iterator(Executable query) {
		return sessionAdmin.getSessionExecutor().iterator(transaction, query);
	}

	public <T> List<T> list(Executable query, Class<T> objectClass) {
		return sessionAdmin.getSessionExecutor().list(transaction, query, objectClass);
	}

	public <T> List<Map<String, Object>> list(Executable query) {
		return sessionAdmin.getSessionExecutor().list(transaction, query);
	}

	public int batch(Executable executable) {
		return sessionAdmin.getSessionExecutor().batch(transaction, executable);
	}

	public int execute(Executable executable) {
		return sessionAdmin.getSessionExecutor().update(transaction, executable);
	}

	public int execute(Executable executable, KeyStore keyStore) {
		return sessionAdmin.getSessionExecutor().update(transaction, executable, keyStore);
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
		return delete(mappedClass, ROOT_ALIAS);
	}

	public Delete delete(Class<?> mappedClass, String tableAlias) {
		return delete(mappedClass, tableAlias, false);
	}

	public Delete delete(Class<?> mappedClass, String tableAlias, boolean cascade) {
		return cascade ? new CascadeDelete(this, mappedClass, tableAlias) : new DeleteImpl(this, mappedClass, tableAlias);
	}

	public Query query(Class<?> mappedClass, String tableAlias) {
		return new QueryImpl(this, mappedClass, tableAlias);
	}

	public Query query(Class<?> mappedClass) {
		return query(mappedClass, ROOT_ALIAS);
	}

	public Query query(Query source, String tableAlias) {
		return new QueryImpl(this, source, tableAlias);
	}

	public Query query(String name) {
		return (Query) sessionAdmin.getQueryCache().getObject(name);
	}

	public KeyStore keyStore(Class<?> mappedClass) {
		return sessionAdmin.getSessionExecutor().keyStore(mappedClass);
	}

	public void cache(String name, Query query) {
		sessionAdmin.getQueryCache().putObject(name, query);
	}

	public void close() {
		transaction.close();
	}

	public SessionAdmin getSessionAdmin() {
		return sessionAdmin;
	}

}
