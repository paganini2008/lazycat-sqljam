package lazycat.series.sqljam;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lazycat.series.converter.TypeConverter;
import lazycat.series.jdbc.JdbcType;
import lazycat.series.jdbc.SqlRunner;
import lazycat.series.jdbc.TypeHandlerRegistry;
import lazycat.series.jdbc.mapper.ColumnIndexRowMapper;
import lazycat.series.jdbc.mapper.ObjectRowMapper;
import lazycat.series.jdbc.mapper.RowMapper;
import lazycat.series.logger.Log;
import lazycat.series.logger.LogFactory;
import lazycat.series.sqljam.transcation.Transaction;

/**
 * SessionExecutorImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SessionExecutorImpl implements SessionExecutor {

	private static final Log logger = LogFactory.getLog(SessionExecutorImpl.class);
	private final SqlRunner sqlRunner = new SqlRunner();
	private final Configuration configuration;

	public SessionExecutorImpl(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setTypeConverter(TypeConverter typeConverter) {
		sqlRunner.setTypeConverter(typeConverter);
	}

	public void setTypeHandlerRegistry(TypeHandlerRegistry typeHandlerRegistry) {
		sqlRunner.setTypeHandlerRegistry(typeHandlerRegistry);
	}

	public <T> List<T> list(Transaction transaction, Executable query, Class<T> objectClass) {
		final ParameterCollector parameterCollector = new PreparedParameterCollector();
		query.setParameters(parameterCollector, configuration);
		return list(transaction, query.getText(configuration), parameterCollector.getParameters(), parameterCollector.getJdbcTypes(),
				objectClass);
	}

	public <T> List<T> list(Transaction transaction, String sql, Object[] parameters, JdbcType[] jdbcTypes, Class<T> objectClass) {
		if (logger.isDebugEnabled()) {
			logger.debug("Exec: " + sql);
		}
		try {
			return sqlRunner.queryForList(transaction.getConnection(), sql, parameters, jdbcTypes, getObjectRowMapper(objectClass));
		} catch (SQLException e) {
			throw new SessionException(e);
		}
	}

	public Iterator<Map<String, Object>> iterator(Transaction transaction, Executable query) {
		final ParameterCollector parameterCollector = new PreparedParameterCollector();
		query.setParameters(parameterCollector, configuration);
		return iterator(transaction, query.getText(configuration), parameterCollector.getParameters(), parameterCollector.getJdbcTypes());
	}

	public Iterator<Map<String, Object>> iterator(Transaction transaction, String sql, Object[] parameters, JdbcType[] jdbcTypes) {
		if (logger.isDebugEnabled()) {
			logger.debug("Exec: " + sql);
		}
		try {
			return sqlRunner.iterator(transaction.getConnection(), sql, parameters, jdbcTypes);
		} catch (SQLException e) {
			throw new SessionException(e);
		}
	}

	public List<Map<String, Object>> list(Transaction transaction, Executable query) {
		final ParameterCollector parameterCollector = new PreparedParameterCollector();
		query.setParameters(parameterCollector, configuration);
		return list(transaction, query.getText(configuration), parameterCollector.getParameters(), parameterCollector.getJdbcTypes());
	}

	public List<Map<String, Object>> list(Transaction transaction, String sql, Object[] parameters, JdbcType[] jdbcTypes) {
		if (logger.isDebugEnabled()) {
			logger.debug("Exec: " + sql);
		}
		try {
			return sqlRunner.queryForList(transaction.getConnection(), sql, parameters, jdbcTypes);
		} catch (SQLException e) {
			throw new SessionException(e);
		}
	}

	public <T> Iterator<T> iterator(Transaction transaction, Executable query, Class<T> objectClass) {
		final ParameterCollector parameterCollector = new PreparedParameterCollector();
		query.setParameters(parameterCollector, configuration);
		return iterator(transaction, query.getText(configuration), parameterCollector.getParameters(), parameterCollector.getJdbcTypes(),
				objectClass);
	}

	public <T> Iterator<T> iterator(Transaction transaction, String sql, Object[] parameters, JdbcType[] jdbcTypes, Class<T> objectClass) {
		if (logger.isDebugEnabled()) {
			logger.debug("Exec: " + sql);
		}
		try {
			return sqlRunner.iterator(transaction.getConnection(), sql, parameters, jdbcTypes, getObjectRowMapper(objectClass));
		} catch (SQLException e) {
			throw new SessionException(e);
		}
	}

	public <T> T first(Transaction transaction, Executable query, final Class<T> objectClass) {
		final ParameterCollector parameterCollector = new PreparedParameterCollector();
		query.setParameters(parameterCollector, configuration);
		return first(transaction, query.getText(configuration), parameterCollector.getParameters(), parameterCollector.getJdbcTypes(),
				objectClass);
	}

	public <T> T first(Transaction transaction, String sql, Object[] parameters, JdbcType[] jdbcTypes, final Class<T> objectClass) {
		if (logger.isDebugEnabled()) {
			logger.debug("Exec: " + sql);
		}
		try {
			return sqlRunner.queryForObject(transaction.getConnection(), sql, parameters, jdbcTypes, getObjectRowMapper(objectClass));
		} catch (SQLException e) {
			throw new SessionException(e);
		}
	}

	public <T> T getResult(Transaction transaction, Executable query, final Class<T> requiredType) {
		final ParameterCollector parameterCollector = new PreparedParameterCollector();
		query.setParameters(parameterCollector, configuration);
		return getResult(transaction, query.getText(configuration), parameterCollector.getParameters(), parameterCollector.getJdbcTypes(),
				requiredType);
	}

	public <T> T getResult(Transaction transaction, String sql, Object[] parameters, JdbcType[] jdbcTypes, Class<T> requiredType) {
		if (logger.isDebugEnabled()) {
			logger.debug("Exec: " + sql);
		}
		try {
			return sqlRunner.queryForObject(transaction.getConnection(), sql, parameters, jdbcTypes,
					new ColumnIndexRowMapper<T>(requiredType, sqlRunner.getTypeConverter()));
		} catch (SQLException e) {
			throw new SessionException(e);
		}
	}

	private <T> RowMapper<T> getObjectRowMapper(Class<T> objectClass) {
		if (configuration.hasMapped(objectClass)) {
			return new TableObjectRowMapper<T>(configuration.getTableDefinition(objectClass));
		} else {
			return new ObjectRowMapper<T>(objectClass, sqlRunner.getTypeConverter());
		}
	}

	public int update(Transaction transaction, Executable executable, KeyStore keyStore) {
		final ParameterCollector parameterCollector = new PreparedParameterCollector();
		try {
			executable.setParameters(parameterCollector, configuration);
			return sqlRunner.update(transaction.getConnection(), executable.getText(configuration), parameterCollector.getParameters(),
					parameterCollector.getJdbcTypes(), keyStore.getKeyHolder());
		} catch (SQLException e) {
			throw new SessionException(e);
		}
	}

	public KeyStore keyStore(Class<?> mappedClass) {
		return new GeneratedKeyStore(configuration.getTableDefinition(mappedClass));
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public int batch(Transaction transaction, Executable executable) {
		final ParameterCollector parameterCollector = new PreparedParameterCollector();
		executable.setParameters(parameterCollector, configuration);
		return batch(transaction, executable.getText(configuration), parameterCollector.getParameterList(),
				parameterCollector.getJdbcTypes());
	}

	public int batch(Transaction transaction, String sql, List<Object[]> parameterList, JdbcType[] jdbcTypes) {
		if (logger.isDebugEnabled()) {
			logger.debug("Exec: " + sql);
		}
		try {
			int[] effects = sqlRunner.batch(transaction.getConnection(), sql, parameterList, jdbcTypes);
			return effects != null ? effects.length : 0;
		} catch (SQLException e) {
			throw new SessionException(e);
		}
	}

	public int update(Transaction transaction, Executable executable) {
		final ParameterCollector parameterCollector = new PreparedParameterCollector();
		executable.setParameters(parameterCollector, configuration);
		return update(transaction, executable.getText(configuration), parameterCollector.getParameters(),
				parameterCollector.getJdbcTypes());
	}

	public int update(Transaction transaction, String sql, Object[] parameters, JdbcType[] jdbcTypes) {
		if (logger.isDebugEnabled()) {
			logger.debug("Exec: " + sql);
		}
		try {
			return sqlRunner.update(transaction.getConnection(), sql, parameters, jdbcTypes);
		} catch (SQLException e) {
			throw new SessionException(e);
		}
	}

	public int update(Transaction transaction, String sql, Object[] arguments) {
		if (logger.isDebugEnabled()) {
			logger.debug("Exec: " + sql);
		}
		try {
			return sqlRunner.update(transaction.getConnection(), sql, arguments);
		} catch (SQLException e) {
			throw new SessionException(e);
		}
	}

}
