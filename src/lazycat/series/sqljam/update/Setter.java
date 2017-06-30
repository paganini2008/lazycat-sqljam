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
import lazycat.series.sqljam.NullIdentifierException;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.PropertySelector;
import lazycat.series.sqljam.PropertySelectors;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.feature.Feature;
import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.TableDefinition;

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
		TableDefinition tableDefinition = translator.getTableDefinition(configuration);
		ColumnDefinition[] columnDefinitions = translator.getColumns(null, configuration);
		List<String> ids = new ArrayList<String>();
		List<String> columns = new ArrayList<String>();
		for (ColumnDefinition cd : columnDefinitions) {
			if (tableDefinition.isPrimaryKey(cd.getMappedProperty())) {
				ids.add(prefix.concat(cd.getColumnName().concat("=?")));
			} else {
				if (exludeProperties.contains(cd.getMappedProperty())) {
					continue;
				}
				Object result = getPropertyValue(object, cd);
				if (propertySelector.include(result, cd.getMappedProperty(), cd.getJdbcType())) {
					columns.add(prefix.concat(cd.getColumnName()).concat("=?"));
				}
			}
		}
		Feature feature = configuration.getJdbcAdmin().getFeature();
		StringBuilder sql = new StringBuilder();
		sql.append(feature.set(CollectionUtils.join(columns, ", "))).append(feature.where(CollectionUtils.join(ids, " and ")));
		return sql.toString();
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		TableDefinition tableDefinition = translator.getTableDefinition(configuration);
		ColumnDefinition[] columnDefinitions = translator.getColumns(null, configuration);
		List<ColumnDefinition> ids = new ArrayList<ColumnDefinition>();
		List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();
		for (ColumnDefinition cd : columnDefinitions) {
			if (tableDefinition.isPrimaryKey(cd.getMappedProperty())) {
				Object result = getPropertyValue(object, cd);
				if (result == null) {
					throw new NullIdentifierException(cd.getColumnName());
				}
				ids.add(cd);
			} else {
				if (exludeProperties.contains(cd.getMappedProperty())) {
					continue;
				}
				Object result = getPropertyValue(object, cd);
				if (propertySelector.include(result, cd.getMappedProperty(), cd.getJdbcType())) {
					columns.add(cd);
				}
			}
		}
		columns.addAll(ids);
		for (ColumnDefinition cd : columns) {
			parameterCollector.setParameter(getPropertyValue(object, cd), cd.getJdbcType());
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
