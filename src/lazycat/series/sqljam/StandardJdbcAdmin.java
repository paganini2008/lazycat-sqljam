package lazycat.series.sqljam;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import lazycat.series.collection.CaseInsensitiveMap;
import lazycat.series.jdbc.DBUtils;
import lazycat.series.jdbc.DataSourceFactory;
import lazycat.series.jdbc.TransactionIsolationLevel;
import lazycat.series.logger.LoggerFactory;
import lazycat.series.logger.MyLogger;
import lazycat.series.sqljam.feature.Feature;
import lazycat.series.sqljam.feature.MySqlFeature;
import lazycat.series.sqljam.feature.PostgreSqlFeature;
import lazycat.series.sqljam.feature.SqlServerFeature;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * StandardJdbcAdmin
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class StandardJdbcAdmin implements JdbcAdmin {

	private static final MyLogger logger = LoggerFactory.getLogger(StandardJdbcAdmin.class);
	private static final Map<String, Feature> featureRegistry = new CaseInsensitiveMap<Feature>();

	static {
		featureRegistry.put("mysql", new MySqlFeature());
		featureRegistry.put("postgresql", new PostgreSqlFeature());
		featureRegistry.put("sqlserver", new SqlServerFeature());
	}

	private final Map<AutoDdl, DdlResolver> ddlResolvers = new EnumMap<AutoDdl, DdlResolver>(AutoDdl.class);
	private final Configuration configuration;
	private final ConnectionProvider connectionProvider;
	private final String databaseProductName;
	private final String databaseProductVersion;
	private final int databaseMinorVersion;
	private final int databaseMajorVersion;
	private final String driverName;
	private final String driverVersion;
	private final int driverMinorVersion;
	private final int driverMajorVersion;
	private final TransactionIsolationLevel transactionIsolationLevel;

	public StandardJdbcAdmin(String driverClassName, String url, String username, String password) {
		this(new Configuration(), new UnpooledConnectionProvider(driverClassName, url, username, password));
	}

	public StandardJdbcAdmin(DataSource dataSource) {
		this(new Configuration(), new PooledConnectionProvider(dataSource));
	}

	public StandardJdbcAdmin(DataSourceFactory factory) throws SQLException {
		this(factory.getDataSource());
	}

	protected StandardJdbcAdmin(Configuration configuration, ConnectionProvider connectionProvider) {

		this.configuration = configuration;
		this.connectionProvider = connectionProvider;

		Connection connection = connectionProvider.openConnectionImplicitly();
		try {
			DatabaseMetaData dbmd = connection.getMetaData();
			this.databaseProductName = dbmd.getDatabaseProductName();
			this.databaseProductVersion = dbmd.getDatabaseProductVersion();
			this.databaseMinorVersion = dbmd.getDatabaseMinorVersion();
			this.databaseMajorVersion = dbmd.getDatabaseMajorVersion();
			this.driverName = dbmd.getDriverName();
			this.driverVersion = dbmd.getDriverVersion();
			this.driverMinorVersion = dbmd.getDriverMinorVersion();
			this.driverMajorVersion = dbmd.getDriverMajorVersion();
			this.transactionIsolationLevel = TransactionIsolationLevel.find(dbmd.getDefaultTransactionIsolation());
		} catch (SQLException e) {
			throw new JdbcFault(e);
		} finally {
			connectionProvider.closeConnection(connection);
		}

		logger.info(
				"DatabaseProductName={}, DatabaseProductVersion={}, DatabaseMajorVersion={}, DatabaseMinorVersion={}, DriverName={}, DriverVersion={}, DriverMajorVersion={}, DriverMinorVersion={}, DefaultTransactionIsolationLevel={}",
				databaseProductName, databaseProductVersion, databaseMajorVersion, databaseMinorVersion, driverName,
				driverVersion, driverMajorVersion, driverMinorVersion, transactionIsolationLevel);

		registerDdlResolver(AutoDdl.DEFAULT, new DefaultDdlResolver());
		registerDdlResolver(AutoDdl.CREATE, new CreateDdlResolver());
		registerDdlResolver(AutoDdl.CREATE_DROP, new CreateDropDdlResolver());
		registerDdlResolver(AutoDdl.UPDATE, new UpdateDdlResolver());
		registerDdlResolver(AutoDdl.VALIDATE, new ValidateDdlResolver());
	}

	public void registerDdlResolver(AutoDdl autoDdl, DdlResolver ddlResolver) {
		ddlResolvers.put(autoDdl, ddlResolver);
	}

	public boolean tableExists(Class<?> mappedClass) {
		TableDefinition tableDefinition = configuration.getMetaData().getTable(mappedClass);
		Connection connection = null;
		try {
			connection = connectionProvider.openConnectionImplicitly();
			return JdbcUtils.tableExists(connection.getMetaData(), tableDefinition.getSchemaDefinition().getCatalog(),
					tableDefinition.getSchemaDefinition().getSchema(), tableDefinition.getTableName());
		} catch (SQLException e) {
			throw new JdbcFault(e);
		} finally {
			connectionProvider.closeConnection(connection);
		}
	}

	public void updateTable(Class<?> mappedClass) {
		TableDefinition tableDefinition = configuration.getMetaData().getTable(mappedClass);
		Connection connection = null;
		try {
			connection = connectionProvider.openConnectionImplicitly();
			String sql;
			for (Iterator<String> iter = configuration.getFeature().iteratorForUpdateDDL(tableDefinition, this,
					connection.getMetaData()); iter.hasNext();) {
				sql = iter.next();
				if (logger.isDebugEnabled()) {
					logger.debug("Execute sql: {}", sql);
				}
				DBUtils.executeUpdate(connection, sql);
			}
		} catch (SQLException e) {
			throw new JdbcFault(e);
		} finally {
			connectionProvider.closeConnection(connection);
		}
	}

	public void createTable(Class<?> mappedClass) {
		TableDefinition tableDefinition = configuration.getMetaData().getTable(mappedClass);
		Connection connection = null;
		try {
			connection = connectionProvider.openConnectionImplicitly();
			String sql;
			for (Iterator<String> iter = configuration.getFeature().iteratorForCreateDDL(tableDefinition, this,
					connection.getMetaData()); iter.hasNext();) {
				sql = iter.next();
				if (logger.isDebugEnabled()) {
					logger.debug("Execute sql: {}", sql);
				}
				DBUtils.executeUpdate(connection, sql);
			}
		} catch (SQLException e) {
			throw new JdbcFault(e);
		} finally {
			connectionProvider.closeConnection(connection);
		}
	}

	public void dropTable(Class<?> mappedClass) {
		TableDefinition tableDefinition = configuration.getMetaData().getTable(mappedClass);
		Connection connection = null;
		try {
			connection = connectionProvider.openConnectionImplicitly();
			String sql;
			for (Iterator<String> iter = configuration.getFeature().iteratorForDropDDL(tableDefinition,
					connection.getMetaData()); iter.hasNext();) {
				sql = iter.next();
				if (logger.isDebugEnabled()) {
					logger.debug("Execute sql: {}", sql);
				}
				DBUtils.executeUpdate(connection, sql);
			}
		} catch (SQLException e) {
			throw new JdbcFault(e);
		} finally {
			connectionProvider.closeConnection(connection);
		}
	}

	public void validateTable(Class<?> mappedClass) {
		TableDefinition tableDefinition = configuration.getMetaData().getTable(mappedClass);
		Connection connection = null;
		try {
			connection = connectionProvider.openConnectionImplicitly();
			configuration.getFeature().validateTable(tableDefinition, this, connection.getMetaData());
		} catch (SQLException e) {
			throw new JdbcFault(e);
		} finally {
			connectionProvider.closeConnection(connection);
		}
	}

	public void resolve(Class<?> mappedClass) {
		TableDefinition tableDefinition = configuration.getMetaData().getTable(mappedClass);
		ddlResolvers.get(tableDefinition.getAutoDdl()).resolve(this, mappedClass);
	}

	public void resolve() {
		final List<Class<?>> hasResolvedTables = new ArrayList<Class<?>>();
		for (Class<?> mappedClass : new ArrayList<Class<?>>(configuration.getMetaData().getAllMappedClasses())) {
			if (!hasResolvedTables.contains(mappedClass)) {
				resolve(mappedClass);
				hasResolvedTables.add(mappedClass);
			}
		}
	}

	public String getDatabaseProductName() {
		return databaseProductName;
	}

	public String getDatabaseProductVersion() {
		return databaseProductVersion;
	}

	public int getDatabaseMinorVersion() {
		return databaseMinorVersion;
	}

	public int getDatabaseMajorVersion() {
		return databaseMajorVersion;
	}

	public String getDriverName() {
		return driverName;
	}

	public String getDriverVersion() {
		return driverVersion;
	}

	public int getDriverMinorVersion() {
		return driverMinorVersion;
	}

	public int getDriverMajorVersion() {
		return driverMajorVersion;
	}

	public TransactionIsolationLevel getDefaultTransactionIsolationLevel() {
		return transactionIsolationLevel;
	}

	public Feature getDefaultFeature() {
		return featureRegistry.get(databaseProductName);
	}

	public ConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public SessionFactory buildSessionEngine(TransactionIsolationLevel transactionIsolationLevel, boolean autoCommit) {
		return new SessionEngine(this, transactionIsolationLevel, autoCommit, null);
	}

	public void closeConnections() {
		connectionProvider.destroy();
	}

}
