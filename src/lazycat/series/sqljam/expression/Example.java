package lazycat.series.sqljam.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lazycat.series.beans.Property;
import lazycat.series.collection.CollectionUtils;
import lazycat.series.lang.Assert;
import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.PropertySelector;
import lazycat.series.sqljam.PropertySelectors;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.relational.ColumnDefinition;

/**
 * Example for query
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Example implements Expression {

	private final Object object;
	private final String tableAlias;
	private final Set<String> excludedProperties = new HashSet<String>();
	private PropertySelector propertySelector = PropertySelectors.NON_NULL;
	private final Map<String, Property<?>> cache = new HashMap<String, Property<?>>();

	public Example(Object object, String tableAlias) {
		Assert.isNull(object, "Null example");
		this.object = object;
		this.tableAlias = tableAlias;
	}

	public Example excludeProperty(String property) {
		excludedProperties.add(property);
		return this;
	}

	public static Example create(Object object) {
		return new Example(object, null);
	}

	public Example setPropertySelector(PropertySelector propertySelector) {
		this.propertySelector = propertySelector;
		return this;
	}

	public String getText(Translator translator, Configuration configuration) {
		final String prefix = StringUtils.isNotBlank(tableAlias) ? tableAlias + "." : "";
		List<String> list = new ArrayList<String>();
		if (translator.hasPrimaryKey(tableAlias, configuration.getMetaData())) {
			ColumnDefinition[] definitions = translator.getPrimaryKeys(tableAlias, configuration.getMetaData());
			for (ColumnDefinition definition : definitions) {
				Object parameter = getPropertyValue(object, definition);
				if (propertySelector.include(parameter, definition.getColumnName(), definition.getJdbcType())) {
					list.add(prefix.concat(definition.getColumnName()).concat("=?"));
				}
			}
			if (list.size() == definitions.length) {
				return CollectionUtils.join(list, " and ");
			}
		}
		ColumnDefinition[] definitions = translator.getColumns(prefix, configuration.getMetaData());
		for (ColumnDefinition definition : definitions) {
			if (excludedProperties.contains(definition.getMappedProperty())) {
				continue;
			}
			Object parameter = getPropertyValue(object, definition);
			if (propertySelector.include(parameter, definition.getColumnName(), definition.getJdbcType())) {
				list.add(prefix.concat(definition.getColumnName()).concat("=?"));
			}
		}
		return CollectionUtils.join(list, " and ");
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		if (translator.hasPrimaryKey(tableAlias, configuration.getMetaData())) {
			ColumnDefinition[] definitions = translator.getPrimaryKeys(tableAlias, configuration.getMetaData());
			boolean sure = true;
			for (ColumnDefinition definition : definitions) {
				Object parameter = getPropertyValue(object, definition);
				if (propertySelector.include(parameter, definition.getColumnName(), definition.getJdbcType())) {
					parameterCollector.setParameter(parameter, definition.getJdbcType());
				} else {
					sure = false;
				}
			}
			if (sure) {
				return;
			}
		}
		ColumnDefinition[] definitions = translator.getPrimaryKeys(tableAlias, configuration.getMetaData());
		for (ColumnDefinition definition : definitions) {
			if (excludedProperties.contains(definition.getMappedProperty())) {
				continue;
			}
			Object parameter = getPropertyValue(object, definition);
			if (propertySelector.include(parameter, definition.getColumnName(), definition.getJdbcType())) {
				parameterCollector.setParameter(parameter, definition.getJdbcType());
			}
		}

	}

	protected Object getPropertyValue(Object object, ColumnDefinition cd) {
		Property<?> property = cache.get(cd.getMappedProperty());
		if (property == null) {
			cache.put(cd.getMappedProperty(), Property.getProperty(cd.getMappedProperty(), (Class<?>) cd.getJavaType()));
			property = cache.get(cd.getMappedProperty());
		}
		return property.extract(object);
	}
}
