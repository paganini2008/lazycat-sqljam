package lazycat.series.sqljam.update;

import lazycat.series.concurrent.FutureCallback;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.KeyStore;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;

public class InsertBackgroundImpl implements InsertBackground {

	private final Session session;
	private final Insert insert;

	public InsertBackgroundImpl(Session session, Insert insert) {
		this.session = session;
		this.insert = insert;
	}

	public void execute() {
		session.getSessionFactory().getThreadPool().submit(new Runnable() {
			public void run() {
				session.execute(insert);
			}
		});
	}

	public void execute(final KeyStore keyStore) {
		session.getSessionFactory().getThreadPool().submit(new Runnable() {
			public void run() {
				session.execute(insert, keyStore);
			}
		});
	}

	public void execute(FutureCallback<Integer> callback) {
		session.getSessionFactory().getThreadPool().submit(new ExecutorCallable() {
			public Integer call() throws Exception {
				return session.execute(insert);
			}
		}, callback);
	}

	public void execute(final KeyStore keyStore, FutureCallback<Integer> callback) {
		session.getSessionFactory().getThreadPool().submit(new ExecutorCallable() {
			public Integer call() throws Exception {
				return session.execute(insert, keyStore);
			}
		}, callback);
	}

	public String getText(Configuration configuration) {
		return insert.getText(configuration);
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		insert.setParameters(parameterCollector, configuration);
	}

}
