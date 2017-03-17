package lazycat.series.sqljam.query;

import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.MetaData;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * Just a simple 'from' like 'from your_table'
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SimpleFrom extends StandardFrom {

	private final Class<?> mappedClass;
	private final String tableAlias;
	private final From last;

	public SimpleFrom(Class<?> mappedClass, String tableAlias) {
		this(mappedClass, tableAlias, null);
	}

	public SimpleFrom(Class<?> mappedClass, String tableAlias, From last) {
		this.mappedClass = mappedClass;
		this.tableAlias = tableAlias;
		this.last = last;
	}

	public String getText(Configuration configuration) {
		TableDefinition tableDefinition = configuration.getMetaData().getTable(mappedClass);
		StringBuilder text = new StringBuilder();
		if (last != null) {
			text.append(last.getText(configuration));
			text.append(",");
		}
		if (StringUtils.isNotBlank(tableAlias)) {
			text.append(configuration.getFeature().as(tableDefinition.getTableName(), tableAlias));
		} else {
			text.append(tableDefinition.getTableName());
		}
		return text.toString();
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		if (last != null) {
			last.setParameters(parameterCollector, configuration);
		}
	}

	public Class<?> findMappedClass(String tableAlias, MetaData metaData) {
		if (StringUtils.isBlank(tableAlias) || this.tableAlias.equals(tableAlias)) {
			return mappedClass;
		}
		if (last != null) {
			return last.findMappedClass(tableAlias, metaData);
		}
		return null;
	}

	public int size() {
		int size = 1;
		if (last != null) {
			size += last.size();
		}
		return size;
	}

	public String getTableAlias() {
		return tableAlias;
	}

}
