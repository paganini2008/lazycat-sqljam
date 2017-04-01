package lazycat.series.sqljam.update;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lazycat.series.concurrent.FutureCallback;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;

/**
 * BatchImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class BatchImpl implements Batch {

	private final List<Object> bag = new CopyOnWriteArrayList<Object>();
	private final Insert insert;
	private final Session session;
	private int flushSize = -1;

	public BatchImpl(Insert insert, Session session) {
		this.insert = insert;
		this.session = session;
	}

	private final AtomicInteger rows = new AtomicInteger(0);

	public Batch setFlushSize(int flushSize) {
		this.flushSize = flushSize;
		return this;
	}

	public Batch push(Object object) {
		if (object != null) {
			bag.add(object);
		}
		if (flushSize > 0 && bag.size() > flushSize) {
			flush();
		}
		return this;
	}

	public void flush() {
		session.getSessionFactory().getThreadPool().submit(new Runnable() {
			public void run() {
				doFlush();
			}
		});
	}

	private void doFlush() {
		List<Object> objectList = new ArrayList<Object>(bag);
		insert.values(objectList);
		int effected = insert.batch(BatchImpl.this);
		rows.addAndGet(effected);
		bag.removeAll(objectList);
	}

	public void execute(FutureCallback<Integer> callback) {
		Configuration configuration = session.getSessionFactory().getConfiguration();
		session.getSessionFactory().getThreadPool().submit(new ExecutorCallable() {
			public Integer call() throws Exception {
				doFlush();
				return rows.get();
			}
		}, configuration.getDefaultSessionTimeout(), TimeUnit.SECONDS, callback);
	}

	public int execute() {
		Configuration configuration = session.getSessionFactory().getConfiguration();
		return session.getSessionFactory().getThreadPool().submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				doFlush();
				return rows.get();
			}
		}, configuration.getDefaultSessionTimeout(), TimeUnit.SECONDS, rows.get());
	}

	public String getText(Configuration configuration) {
		return insert.getText(configuration);
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		insert.setParameters(parameterCollector, configuration);
	}

}
