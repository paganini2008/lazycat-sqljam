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
	private int flushSize = 100;

	public BatchImpl(Session session, Insert insert) {
		super(session);
		this.insert = insert;
	}

	private final AtomicInteger rows = new AtomicInteger(0);

	public Batch setFlushSize(int size) {
		this.flushSize = Math.max(size, flushSize);
		return this;
	}

	public Batch push(Object object) {
		if (object != null) {
			bag.add(object);
		}
		if (bag.size() > flushSize) {
			execute();
		}
		return this;
	}

	public int flush() {
		List<Object> objectList = new ArrayList<Object>(bag);
		insert.values(objectList);
		int effected = insert.batch(this);
		rows.addAndGet(effected);
		bag.removeAll(objectList);
		return objectList.size();
	}

	public int execute() {
		flush();
		return rows.get();
	}

	public String getText(Configuration configuration) {
		return insert.getText(configuration);
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		insert.setParameters(parameterCollector, configuration);
	}

}
