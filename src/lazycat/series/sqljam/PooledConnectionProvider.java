package lazycat.series.sqljam;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * PooledConnectionProvider
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class PooledConnectionProvider extends AbstractConnectionProvider {

	private final DataSource dataSource;

	public PooledConnectionProvider(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Connection openConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new ConnectionException(e);
		}
	}

}
