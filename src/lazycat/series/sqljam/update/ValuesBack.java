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
import lazycat.series.sqljam.NullValueException;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.generator.Generator;
import lazycat.series.sqljam.relational.ColumnDefinition;

/**
 * ValueList
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ValuesBack implements Expression {

	private final Object object;
	private final ValuesBack last;

	public ValuesBack(Object object, ValuesBack last) {
		Assert.isNull(object, "Null example");
		this.object = object;
		this.last = last;
	}

	private final Set<String> excludeProperties = new HashSet<String>();
	private final Map<String, Property<?>> cache = new HashMap<String, Property<?>>();

	public static ValuesBack create(Object object) {
		return new ValuesBack(object, null);
	}

	public ValuesBack excludeProperties(String... propertyNames) {
		if (propertyNames != null) {
			excludeProperties.addAll(Arrays.asList(propertyNames));
		}
		return this;
	}

	public String getText(Translator translator, Configuration configuration) {
		List<String> list = new ArrayList<String>();
		ColumnDefinition[] definitions = translator.getColumns(null, configuration.getMetaData());
		for (ColumnDefinition cd : definitions) {
			if (excludeProperties.contains(cd.getMappedProperty())) {
				continue;
			}
			if (cd.isAutoIncrement()) {
				continue;
			}
			if (cd.getIdentifierGenerator() != null) {
				String text = cd.getIdentifierGenerator().getText(configuration.getFeature(), translator.getCurrentSession());
				if (StringUtils.isNotBlank(text)) {
					list.add(text);
				}
			} else {
				Object value = getPropertyValue(object, cd);
				if (value != null || cd.isNullable()) {
					list.add("?");
				} else if (StringUtils.isBlank(cd.getDefaultValue())) {
					if (StringUtils.isNotBlank(cd.getDefaultValue())) {
						continue;
					}
					if (StringUtils.isNotBlank(cd.getInsertSql())) {
						list.add(cd.getInsertSql());
					}
				}
			}
		}
		String joinStr = CollectionUtils.join(list, ",");
		if (last != null) {
			joinStr = last.getText(translator, configuration) + "," + configuration.getFeature().wrap(joinStr);
		} else {
			joinStr = configuration.getFeature().wrap(joinStr);
		}
		return joinStr;
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		if (last != null) {
			last.setParameter(translator, parameterCollector, configuration);
		}
		ColumnDefinition[] definitions = translator.getColumns(null, configuration.getMetaData());
		for (ColumnDefinition cd : definitions) {
			if (excludeProperties.contains(cd.getMappedProperty())) {
				continue;
			}
			if (cd.isAutoIncrement()) {
				continue;
			}
			Object value = null;
			Generator identifier = cd.getIdentifierGenerator();
			if (identifier != null) {
				if (identifier.hasValue(configuration.getFeature(), translator.getCurrentSession())) {
					value = identifier.getValue(configuration.getFeature(), translator.getCurrentSession());
				}
			} else {
				value = getPropertyValue(object, cd);
			}
			if (value != null || cd.isNullable()) {
				parameterCollector.setParameter(value, cd.getJdbcType());
			} else {
				if (StringUtils.isNotBlank(cd.getDefaultValue())) {
					continue;
				}
				if (identifier != null && identifier.hasValue(configuration.getFeature(), translator.getCurrentSession()) == false) {
					continue;
				}
				if (translator.isPrimaryKey(null, cd.getMappedProperty(), configuration.getMetaData())) {
					throw new NullValueException("PrimaryKey '" + cd.getMappedProperty() + "' must not be null.");
				}
				if (StringUtils.isBlank(cd.getDefaultValue())) {
					throw new NullValueException("Property '" + cd.getMappedProperty() + "' must not be null.");
				}
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
