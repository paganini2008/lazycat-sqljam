package lazycat.series.sqljam;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.query.Query;
import lazycat.series.sqljam.update.Delete;
import lazycat.series.sqljam.update.Insert;
import lazycat.series.sqljam.update.Update;

/**
 * Session
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Session {

	final static String ROOT_ALIAS = "this";

	KeyStore keyStore(Class<?> mappedClass);

	int save(Object o);

	int update(Object o);

	<T> T get(Serializable id, Class<T> beanClass);

	int delete(Object o);

	int delete(Object o, boolean cascaded);

	<T> T getResult(Executable query, Class<T> requiredType);

	<T> T getResult(String sql, Object[] parameters, JdbcType[] jdbcTypes, Class<T> requiredType);

	<T> T first(Executable query, Class<T> beanClass);

	<T> List<T> list(Executable query, Class<T> objectClass);

	<T> List<Map<String, Object>> list(Executable query);

	<T> Iterator<T> iterator(Executable query, Class<T> objectClass);

	<T> Iterator<Map<String, Object>> iterator(Executable query);

	Insert insert(Class<?> mappedClass);

	Delete delete(Class<?> mappedClass);

	Delete delete(Class<?> mappedClass, String tableAlias);

	Delete delete(Class<?> mappedClass, String tableAlias, boolean cascade);

	Update update(Class<?> mappedClass);

	Update update(Class<?> mappedClass, String tableAlias);

	Query query(Class<?> mappedClass);

	Query query(Class<?> mappedClass, String tableAlias);

	Query query(Query query, String tableAlias);

	int batch(Executable executable);

	int execute(Executable executable);

	int execute(Executable executable, KeyStore keyStore);

	int executeSql(String sql, Object[] arguments);

	void rollback();

	void commit();

	void close();

	void cache(String name, Query query);

	Query query(String name);

	SessionAdmin getSessionAdmin();

}
