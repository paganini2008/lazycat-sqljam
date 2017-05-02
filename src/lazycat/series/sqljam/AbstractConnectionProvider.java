package lazycat.series.sqljam;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * AbstractConnectionProvider
 * 
 * @author Fred Feng
 * @version 1.0
 */
public abstract class AbstractConnectionProvider implements ConnectionProvider {

	private ProductMetadata productMetadata;

	public ProductMetadata getMetadata() {
		if (productMetadata == null) {
			productMetadata = new ProductMetadataService(this);
		}
		return productMetadata;
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

	public void destroy() {
	}

}
