package lazycat.series.sqljam.query;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;

/**
 * Intersect Clause
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Intersect extends AbstractQuery {

	public Intersect(From left, From right) {
		this.left = left;
		this.right = right;
	}

	private final From left;
	private final From right;

	public String getText(Configuration configuration) {
		return configuration.getJdbcAdmin().getFeature().intersect(left.getText(configuration), right.getText(configuration));
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		left.setParameters(parameterCollector, configuration);
		right.setParameters(parameterCollector, configuration);
	}

	public Class<?> findMappedClass(String tableAlias, Configuration metaData) {
		Class<?> type = left.findMappedClass(tableAlias, metaData);
		return type != null ? type : right.findMappedClass(tableAlias, metaData);
	}

	public int size() {
		int size = left.size();
		size += right.size();
		return size;
	}

}
