package lazycat.series.sqljam.update;

import java.util.ArrayList;
import java.util.Arrays;
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
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.relational.ColumnDefinition;

/**
 * Setter
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Setter implements Expression {

	private final Object object;
	private final Map<String, Property> cache = new HashMap<String, Property>();

	public Setter(Object object) {
		Assert.isNull(object, "Null example");
		this.object = object;
	}

	private PropertySelector propertySelector = PropertySelectors.NON_NULL;
	private final Set<String> exludeProperties = new HashSet<String>();

	public Setter setPropertySelector(PropertySelector propertySelector) {
		this.propertySelector = propertySelector;
		return this;
	}

	public Setter excludeProperties(String... excludeProperties) {
		if (excludeProperties != null) {
			exludeProperties.addAll(Arrays.asList(excludeProperties));
		}
		return this;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		String tableAlias = translator.getTableAlias();
		String prefix = StringUtils.isNotBlank(tableAlias) ? tableAlias + "." : "";
		ColumnDefinition[] definitions = translator.getColumns(null, configuration);
		List<String> list = new ArrayList<String>();
		for (ColumnDefinition cd : definitions) {
			if (exludeProperties.contains(cd.getMappedProperty())) {
				continue;
			}
			Object result = getPropertyValue(object, cd);
			if (propertySelector.include(result, cd.getMappedProperty(), cd.getJdbcType())) {
				list.add(prefix.concat(cd.getColumnName()).concat("=?"));
			}
		}
		return configuration.getJdbcAdmin().getFeature().set(CollectionUtils.join(list, ", "));
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		ColumnDefinition[] definitions = translator.getColumns(null, configuration);
		for (ColumnDefinition cd : definitions) {
			if (exludeProperties.contains(cd.getMappedProperty())) {
				continue;
			}
			Object result = getPropertyValue(object, cd);
			if (propertySelector.include(result, cd.getMappedProperty(), cd.getJdbcType())) {
				parameterCollector.setParameter(result, cd.getJdbcType());
			}
		}
	}

	protected Object getPropertyValue(Object object, ColumnDefinition cd) {
		Property property = cache.get(cd.getMappedProperty());
		if (property == null) {
			cache.put(cd.getMappedProperty(), Property.getProperty(cd.getMappedProperty(), (Class<?>) cd.getJavaType()));
			property = cache.get(cd.getMappedProperty());
		}
		return property.extract(object);
	}

}
