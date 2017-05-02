package lazycat.series.sqljam;

import java.util.concurrent.TimeUnit;

import lazycat.series.concurrent.Call;
import lazycat.series.concurrent.ThreadPool;
import lazycat.series.concurrent.ThreadPoolBuilder;
import lazycat.series.concurrent.ThreadUtils;

/**
 * SessionPool
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SessionPool {

	private static final String NAME = "sessions";

	public SessionPool(int nThreads, int maxPermits, long defaultTimeout) {
		final ThreadPoolBuilder builder = new ThreadPoolBuilder(nThreads).setThreadFactory(ThreadUtils.pooledThreadFactory(NAME));
		this.threadPool = new ThreadPool(builder, maxPermits, 60);
		this.defaultTimeout = defaultTimeout;
	}

	private final ThreadPool threadPool;
	private final long defaultTimeout;

	public void execute(Runnable task) {
		threadPool.apply(task);
	}

	public Object execute(Call task) {
		return execute(task, defaultTimeout);
	}

	public Object execute(Call task, long timeout) {
		return threadPool.apply(task, timeout, TimeUnit.MILLISECONDS);
	}

	public void close() {
		threadPool.shutdown();
	}

}
