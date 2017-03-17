package lazycat.series.sqljam.query;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.MetaData;
import lazycat.series.sqljam.ParameterCollector;

/**
 * Union all clause
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class UnionAll extends AbstractQuery {

	public UnionAll(From left, From right) {
		this.left = left;
		this.right = right;
	}

	private final From left;
	private final From right;

	public String getText(Configuration configuration) {
		return configuration.getFeature().unionAll(left.getText(configuration), right.getText(configuration));
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		left.setParameters(parameterCollector, configuration);
		right.setParameters(parameterCollector, configuration);
	}

	public Class<?> findMappedClass(String tableAlias, MetaData metaData) {
		Class<?> type = left.findMappedClass(tableAlias, metaData);
		return type != null ? type : right.findMappedClass(tableAlias, metaData);
	}

	public int size() {
		int size = left.size();
		size += right.size();
		return size;
	}

}