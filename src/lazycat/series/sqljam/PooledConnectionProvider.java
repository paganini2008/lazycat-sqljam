package lazycat.series.sqljam;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class PooledConnectionProvider implements ConnectionProvider {

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

	public void closeConnection(Connection connection) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			throw new ConnectionException(e);
		}
	}

	public Connection openConnectionImplicitly() {
		Connection connection = openConnection();
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			throw new ConnectionException(e);
		}
		return connection;
	}

}
