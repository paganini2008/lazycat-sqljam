package lazycat.series.sqljam;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import lazycat.series.jdbc.DBUtils;
import lazycat.series.jdbc.DataSourceFactory;
import lazycat.series.logger.Log;
import lazycat.series.logger.LogFactory;
import lazycat.series.sqljam.feature.Feature;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * StandardJdbcAdmin
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class StandardJdbcAdmin implements JdbcAdmin {

	private static final Log logger = LogFactory.getLog(StandardJdbcAdmin.class);
	private final ConnectionProvider connectionProvider;
	private final Configuration configuration;
	private final Map<AutoDdl, DdlResolver> ddlResolvers = new EnumMap<AutoDdl, DdlResolver>(AutoDdl.class);
	private FeatureFactory featureFactory = new InternalFeatureFactory();
	private Feature feature;

	public StandardJdbcAdmin(String driverClassName, String url, String username, String password) {
		this(new UnpooledConnectionProvider(driverClassName, url, username, password));
	}

	public StandardJdbcAdmin(DataSource dataSource) {
		this(new PooledConnectionProvider(dataSource));
	}

	public StandardJdbcAdmin(DataSourceFactory factory) throws SQLException {
		this(factory.getDataSource());
	}

	protected StandardJdbcAdmin(ConnectionProvider connectionProvider) {
		this.feature = featureFactory.buildFeature(connectionProvider.getMetadata());
		this.connectionProvider = connectionProvider;
		this.configuration = new TableConfiguation(this);

		registerDdlResolver(AutoDdl.DEFAULT, new DefaultDdlResolver());
		registerDdlResolver(AutoDdl.CREATE, new CreateDdlResolver());
		registerDdlResolver(AutoDdl.CREATE_DROP, new CreateDropDdlResolver());
		registerDdlResolver(AutoDdl.UPDATE, new UpdateDdlResolver());
		registerDdlResolver(AutoDdl.VALIDATE, new ValidateDdlResolver());
	}

	public void registerDdlResolver(AutoDdl autoDdl, DdlResolver ddlResolver) {
		ddlResolvers.put(autoDdl, ddlResolver);
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeatureFactory(FeatureFactory featureFactory) {
		this.featureFactory = featureFactory;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public boolean tableExists(Class<?> mappedClass) {
		TableDefinition tableDefinition = configuration.getTableDefinition(mappedClass);
		Connection connection = null;
		try {
			connection = connectionProvider.openConnectionImplicitly();
			return JdbcUtils.tableExists(connection.getMetaData(), null, tableDefinition.getSchema(), tableDefinition.getTableName());
		} catch (SQLException e) {
			throw new JdbcException(e);
		} finally {
			connectionProvider.closeConnection(connection);
		}
	}

	public void alterTable(Class<?> mappedClass) {
		TableDefinition tableDefinition = configuration.getTableDefinition(mappedClass);
		Connection connection = null;
		try {
			connection = connectionProvider.openConnectionImplicitly();
			for (String sql : feature.listForUpdateDDL(tableDefinition, this, connection.getMetaData())) {
				if (logger.isDebugEnabled()) {
					logger.debug("Execute sql: {}", sql);
				}
				DBUtils.executeUpdate(connection, sql);
			}
		} catch (SQLException e) {
			throw new JdbcException(e);
		} finally {
			connectionProvider.closeConnection(connection);
		}
	}

	public void createTable(Class<?> mappedClass) {
		TableDefinition tableDefinition = configuration.getTableDefinition(mappedClass);
		Connection connection = null;
		try {
			connection = connectionProvider.openConnectionImplicitly();
			for (String sql : feature.listForCreateDDL(tableDefinition, this, connection.getMetaData())) {
				if (logger.isDebugEnabled()) {
					logger.debug("Execute sql: {}", sql);
				}
				DBUtils.executeUpdate(connection, sql);
			}
		} catch (SQLException e) {
			throw new JdbcException(e);
		} finally {
			connectionProvider.closeConnection(connection);
		}
	}

	public void dropTable(Class<?> mappedClass) {
		TableDefinition tableDefinition = configuration.getTableDefinition(mappedClass);
		Connection connection = null;
		try {
			connection = connectionProvider.openConnectionImplicitly();
			for (String sql : feature.listForDropDDL(tableDefinition, connection.getMetaData())) {
				if (logger.isDebugEnabled()) {
					logger.debug("Execute sql: {}", sql);
				}
				DBUtils.executeUpdate(connection, sql);
			}
		} catch (SQLException e) {
			throw new JdbcException(e);
		} finally {
			connectionProvider.closeConnection(connection);
		}
	}

	public void validateTable(Class<?> mappedClass) {
		TableDefinition tableDefinition = configuration.getTableDefinition(mappedClass);
		Connection connection = null;
		try {
			connection = connectionProvider.openConnectionImplicitly();
			feature.validateTable(tableDefinition, this, connection.getMetaData());
		} catch (SQLException e) {
			throw new JdbcException(e);
		} finally {
			connectionProvider.closeConnection(connection);
		}
	}

	public void resolve(Class<?> mappedClass) {
		TableDefinition tableDefinition = configuration.getTableDefinition(mappedClass);
		ddlResolvers.get(tableDefinition.getAutoDdl()).resolve(this, mappedClass);
	}

	public void resolve() {
		List<Class<?>> hasResolved = new ArrayList<Class<?>>();
		for (Class<?> mappedClass : configuration.getAllMappedClasses()) {
			if (!hasResolved.contains(mappedClass)) {
				resolve(mappedClass);
				hasResolved.add(mappedClass);
			}
		}
	}

	public ConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}

	public void releaseConnections() {
		connectionProvider.destroy();
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public SessionFactory buildSessionEngine() {
		return new SessionEngine(this, null);
	}

	public void truncateTable(Class<?> mappedClass) {

	}

	public void exportTable(Class<?> mappedClass, String path) {

	}

}
