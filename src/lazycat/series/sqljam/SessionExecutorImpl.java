package lazycat.series.sqljam;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import lazycat.series.converter.TypeConverter;
import lazycat.series.jdbc.ArrayParameterSource;
import lazycat.series.jdbc.DefaultTokenParser;
import lazycat.series.jdbc.JdbcType;
import lazycat.series.jdbc.ParsedSqlRunner;
import lazycat.series.jdbc.TypeHandlerRegistry;
import lazycat.series.jdbc.mapper.ColumnIndexRowMapper;
import lazycat.series.jdbc.mapper.ObjectRowMapper;
import lazycat.series.jdbc.mapper.RowMapper;
import lazycat.series.logger.LazyLogger;
import lazycat.series.logger.LoggerFactory;
import lazycat.series.sqljam.transcation.Transaction;

/**
 * SessionExecutorImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SessionExecutorImpl implements SessionExecutor {

	private static final LazyLogger logger = LoggerFactory.getLogger(SessionExecutor.class);
	private final Configuration configuration;
	private final ParsedSqlRunner sqlRunner;

	public SessionExecutorImpl(Configuration configuration) {
		this.configuration = configuration;
		this.sqlRunner = new ParsedSqlRunner(new DefaultTokenParser("?"), 1024);
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

	public <T> List<T> list(Transaction transaction, Executable query, RowMapper<T> rowMapper) {
		final ParameterCollector parameterCollector = new PreparedParameterCollector();
		query.setParameters(parameterCollector, configuration);
		return list(transaction, query.getText(configuration), parameterCollector.getParameters(), parameterCollector.getJdbcTypes(),
				rowMapper);
	}

	public <T> List<T> list(Transaction transaction, String sql, Object[] parameters, JdbcType[] jdbcTypes, RowMapper<T> rowMapper) {
		if (logger.isDebugEnabled()) {
			logger.debug("Exec: " + sql);
		}
		try {
			return sqlRunner.queryForList(transaction.getConnection(), sql, parameters, jdbcTypes, rowMapper);
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
		if (configuration.getMetaData().hasMapped(objectClass)) {
			return new TableObjectRowMapper<T>(configuration.getMetaData().getTable(objectClass));
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

	public KeyStore newKeyStore(Class<?> mappedClass) {
		return new GeneratedKeyStore(configuration.getMetaData().getTable(mappedClass));
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
			return sqlRunner.update(transaction.getConnection(), sql, arguments != null ? new ArrayParameterSource(arguments) : null);
		} catch (SQLException e) {
			throw new SessionException(e);
		}
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public TypeConverter getTypeConverter() {
		return sqlRunner.getTypeConverter();
	}

	public TypeHandlerRegistry getTypeHandlerRegistry() {
		return sqlRunner.getTypeHandlerRegistry();
	}

}
