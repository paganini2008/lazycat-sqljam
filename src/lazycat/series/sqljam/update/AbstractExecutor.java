package lazycat.series.sqljam.update;

import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.SessionPool;

/**
 * AbstractExecutor
 * 
 * @author Fred Feng
 * @version 1.0
 */
public abstract class AbstractExecutor implements Executor {

	protected final Session session;

	protected AbstractExecutor(Session session) {
		this.session = session;
	}

	public int execute() {
		return session.execute(this);
	}

	public void executeAsync() {
		SessionPool sessionPool = session.getSessionAdmin().getSessionPool();
		sessionPool.execute(new Runnable() {
			public void run() {
				execute();
			}
		});
	}

}
