package lazycat.series.sqljam.update;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;

/**
 * BatchImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class BatchImpl extends AbstractExecutor implements Batch {

	private final List<Object> bag = new CopyOnWriteArrayList<Object>();
	private final Insert insert;
	private int flushSize = -1;

	public BatchImpl(Session session, Insert insert) {
		super(session);
		this.insert = insert;
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
		session.getSessionAdmin().getSessionPool().execute(new Runnable() {
			public void run() {
				doFlush();
			}
		});
	}

	private void doFlush() {
		List<Object> objectList = new ArrayList<Object>(bag);
		insert.values(objectList);
		int effected = insert.batch(this);
		rows.addAndGet(effected);
		bag.removeAll(objectList);
	}

	public int execute() {
		doFlush();
		return rows.get();
	}

	public String getText(Configuration configuration) {
		return insert.getText(configuration);
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		insert.setParameters(parameterCollector, configuration);
	}

}
