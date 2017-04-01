package lazycat.series.sqljam.update;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import lazycat.series.concurrent.FutureCallback;
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
		Configuration configuration = session.getSessionFactory().getConfiguration();
		return session.getSessionFactory().getThreadPool().submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.execute(CreateAs.this);
			}
		}, configuration.getDefaultSessionTimeout(), TimeUnit.SECONDS, 0);
	}

	public void execute(FutureCallback<Integer> callback) {
		Configuration configuration = session.getSessionFactory().getConfiguration();
		session.getSessionFactory().getThreadPool().submit(new ExecutorCallable() {
			public Integer call() throws Exception {
				return session.execute(CreateAs.this);
			}
		}, configuration.getDefaultSessionTimeout(), TimeUnit.SECONDS, callback);
	}

}
