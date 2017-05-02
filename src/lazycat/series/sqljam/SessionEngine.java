package lazycat.series.sqljam;

import java.sql.SQLException;

import javax.sql.DataSource;

import lazycat.series.jdbc.DataSourceFactory;
import lazycat.series.logger.LoggerFactory;
import lazycat.series.logger.MyLogger;

/**
 * SessionEngine
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SessionEngine implements SessionFactory {

	private static final MyLogger logger = LoggerFactory.getLogger(SessionEngine.class);

	private final SessionAdmin sessionAdmin;
	private final JdbcAdmin jdbcAdmin;

	public SessionEngine(String driverClassName, String url, String username, String password,
			ConfigurationInitializer configurationInitializer) {
		this(new StandardJdbcAdmin(driverClassName, url, username, password), configurationInitializer);
	}

	public SessionEngine(DataSource dataSource, ConfigurationInitializer configurationInitializer) {
		this(new StandardJdbcAdmin(dataSource), configurationInitializer);
	}

	public SessionEngine(DataSourceFactory factory, ConfigurationInitializer configurationInitializer) throws SQLException {
		this(new StandardJdbcAdmin(factory), configurationInitializer);
	}

	protected SessionEngine(JdbcAdmin jdbcAdmin, ConfigurationInitializer configurationInitializer) {
		final SessionOptions sessionOptions = new SessionOptions();
		if (configurationInitializer != null) {
			configurationInitializer.configure(jdbcAdmin.getConfiguration(), sessionOptions);
		}
		this.sessionAdmin = new StandardSessionAdmin(sessionOptions, jdbcAdmin.getConfiguration(), jdbcAdmin.getConnectionProvider());
		jdbcAdmin.resolve();
		this.jdbcAdmin = jdbcAdmin;
	}

	public JdbcAdmin getJdbcAdmin() {
		return jdbcAdmin;
	}

	public Session openSession() {
		return sessionAdmin.openSession();
	}

	public void destroy() {
		sessionAdmin.releaseExternalResources();
		jdbcAdmin.releaseConnections();
	}
}
