package lazycat.series.sqljam;

import java.sql.Connection;

/**
 * Provide a jdbc connection from your settings
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface ConnectionProvider {
	
	ProductMetadata getMetadata();

	Connection openConnection();

	Connection openConnectionImplicitly();

	void closeConnection(Connection connection);
	
	void destroy();

}
