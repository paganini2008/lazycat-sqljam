package lazycat.series.sqljam.query;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;

/**
 * View
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class View extends StandardFrom {

	private final From query;
	private final String tableAlias;
	private final From last;

	public View(From query, String tableAlias) {
		this(query, tableAlias, null);
	}

	public View(From query, String tableAlias, From last) {
		this.query = query;
		this.tableAlias = tableAlias;
		this.last = last;
	}

	public String getText(Configuration configuration) {
		StringBuilder text = new StringBuilder();
		if (last != null) {
			text.append(last.getText(configuration));
			text.append(", ");
		}
		String q = configuration.getJdbcAdmin().getFeature().wrap(query.getText(configuration));
		text.append(configuration.getJdbcAdmin().getFeature().tableAs(q, tableAlias));
		return text.toString();
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		query.setParameters(parameterCollector, configuration);
		if (last != null) {
			last.setParameters(parameterCollector, configuration);
		}
	}

	public Class<?> findMappedClass(String tableAlias, Configuration metaData) {
		Class<?> type = query.findMappedClass(tableAlias, metaData);
		if (type != null) {
			return type;
		}
		if (last != null) {
			return last.findMappedClass(tableAlias, metaData);
		}
		return null;
	}

	public int size() {
		int size = query.size();
		if (last != null) {
			size += last.size();
		}
		return size;
	}

	public String getTableAlias() {
		return tableAlias;
	}

}
