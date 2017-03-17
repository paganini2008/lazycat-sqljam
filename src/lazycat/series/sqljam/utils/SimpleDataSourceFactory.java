package lazycat.series.sqljam.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import lazycat.series.io.IOUtils;
import lazycat.series.jdbc.DataSourceFactory;
import lazycat.series.jdbc.TransactionIsolationLevel;
import lazycat.series.lang.StringUtils;

/**
 * SimpleDataSourceFactory
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SimpleDataSourceFactory implements DataSourceFactory {

	private Properties config;

	public void setProperties(Properties config) throws SQLException {
		this.config = config;
	}

	public DataSource getDataSource() throws SQLException {
		SimpleDataSource ds = new SimpleDataSource();
		ds.setDriverClassName(config.getProperty("driverClassName"));
		ds.setUser(config.getProperty("user"));
		ds.setPassword(config.getProperty("password"));
		ds.setUrl(config.getProperty("url"));
		ds.setMaxSize(Integer.parseInt(config.getProperty("maxSize", "16")));
		String arg = config.getProperty("autoCommit");
		if (StringUtils.isNotBlank(arg)) {
			ds.setAutoCommit(Boolean.parseBoolean(arg));
		}
		arg = config.getProperty("defaultTransactionIsolationLevel");
		if (StringUtils.isNotBlank(arg)) {
			ds.setDefaultTransactionIsolationLevel(TransactionIsolationLevel.valueOf(arg.toUpperCase()).getLevel());
		}
		return ds;
	}

	public static DataSourceFactory create(String configName) throws SQLException {
		try {
			return create(getConfig(configName));
		} catch (IOException e) {
			throw new SQLException("Failed to read config file: " + configName, e);
		}
	}

	public static DataSourceFactory create(Properties config) throws SQLException {
		DataSourceFactory dataSourceFactory = new SimpleDataSourceFactory();
		dataSourceFactory.setProperties(config);
		return dataSourceFactory;
	}

	private static Properties getConfig(String configName) throws IOException {
		InputStream in = null;
		try {
			in = SimpleDataSourceFactory.class.getClassLoader().getResourceAsStream(configName);
			Properties config = new Properties();
			config.load(in);
			return config;
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

}
