package lazycat.series.sqljam.update;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;

/**
 * BatchImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class BatchImpl implements Batch {

	private final List<Object> bag = new CopyOnWriteArrayList<Object>();
	private final Insert insert;
	private final int flushSize;

	public BatchImpl(Insert insert, int flushSize) {
		this.insert = insert;
		this.flushSize = flushSize;
	}

	private volatile int rows = 0;

	public Batch push(Object object) {
		if (object != null) {
			bag.add(object);
		}
		if (bag.size() >= flushSize) {
			List<Object> objectList = new ArrayList<Object>(bag);
			flush(objectList);
		}
		return this;
	}

	private void flush(List<Object> objectList) {
		insert.values(objectList);
		rows += insert.batch(this);
		bag.removeAll(objectList);
	}

	public int execute() {
		List<Object> objectList = new ArrayList<Object>(bag);
		flush(objectList);
		return rows;
	}

	public String getText(Configuration configuration) {
		return insert.getText(configuration);
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		insert.setParameters(parameterCollector, configuration);
	}

}
