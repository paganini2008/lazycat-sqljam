package lazycat.series.sqljam;

import java.util.List;

import lazycat.series.jdbc.JdbcType;

/**
 * Collector some sql parameters for your sql
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface ParameterCollector {

	void addBatch();

	void setParameter(Object parameter, JdbcType jdbcType);

	int getBatchSize();

	Object[] getParameters();

	JdbcType[] getJdbcTypes();

	List<Object[]> getParameterList();

}