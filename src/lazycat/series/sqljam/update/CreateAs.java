package lazycat.series.sqljam.update;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.query.ResultSet;

/**
 * CreateAs
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class CreateAs implements Executor {

	private final ResultSet resultSet;
	private final String tableName;
	private final Session session;

	public CreateAs(Session session, ResultSet resultSet, String tableName) {
		this.session = session;
		this.resultSet = resultSet;
		this.tableName = tableName;
	}

	public String getText(Configuration configuration) {
		return configuration.getFeature().createTableAs(tableName, resultSet.getText(configuration));
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		resultSet.setParameters(parameterCollector, configuration);
	}

	public int execute() {
		return session.execute(this);
	}

}
