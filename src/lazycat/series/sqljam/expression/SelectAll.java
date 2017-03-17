package lazycat.series.sqljam.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lazycat.series.collection.CollectionUtils;
import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.feature.Feature;
import lazycat.series.sqljam.relational.ColumnDefinition;

/**
 * SelectAll
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SelectAll implements Expression {

	private final String tableAlias;
	private final boolean as;
	private final Set<String> excludeProperties = new HashSet<String>();

	public SelectAll() {
		this(true);
	}

	public SelectAll(String tableAlias) {
		this(tableAlias, true);
	}

	public SelectAll(boolean as) {
		this("", as);
	}

	public SelectAll(String tableAlias, boolean as) {
		this.tableAlias = tableAlias;
		this.as = as;
	}

	public SelectAll excludeProperties(String... propertyNames) {
		if (propertyNames != null) {
			for (String propertyName : propertyNames) {
				excludeProperties.add(propertyName);
			}
		}
		return this;
	}

	public String getText(Translator translator, Configuration configuration) {
		final String tableAlias = translator.getTableAlias(this.tableAlias);
		String prefix = StringUtils.isNotBlank(tableAlias) ? tableAlias + "." : "";
		List<String> list = new ArrayList<String>();
		ColumnDefinition[] definitions = translator.getColumns(tableAlias, configuration.getMetaData());
		Feature feature = configuration.getFeature();
		for (ColumnDefinition cd : definitions) {
			if (!excludeProperties.contains(cd.getMappedProperty())) {
				String columnName = prefix + cd.getColumnName();
				list.add(as ? feature.as(columnName, cd.getMappedProperty()) : columnName);
			}
		}
		return CollectionUtils.join(list, ",");
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
