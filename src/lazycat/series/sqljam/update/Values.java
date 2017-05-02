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
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.NullValueException;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.Data;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.generator.Generator;
import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.DefaultDefinition;

/**
 * Values
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Values implements Expression {

	private final Object object;

	public Values(Object object) {
		Assert.isNull(object, "Null example.");
		this.object = object;
	}

	private final Set<String> excludeProperties = new HashSet<String>();
	private final Map<String, Property> cache = new HashMap<String, Property>();
	private boolean ignoreAutocrement = true;

	public Values ignoreAutocrement(boolean ignoreAutocrement) {
		this.ignoreAutocrement = ignoreAutocrement;
		return this;
	}

	public Values excludeProperties(String... propertyNames) {
		if (propertyNames != null) {
			excludeProperties.addAll(Arrays.asList(propertyNames));
		}
		return this;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		final List<String> columns = new ArrayList<String>();
		final List<String> args = new ArrayList<String>();
		ColumnDefinition[] columnDefinitions = translator.getColumns(null, configuration);
		for (ColumnDefinition columnDefinition : columnDefinitions) {
			if (excludeProperties.contains(columnDefinition.getMappedProperty())) {
				continue;
			}
			if (ignoreAutocrement && columnDefinition.isAutoIncrement()) {
				continue;
			}
			DefaultDefinition defaultDefinition = columnDefinition.getTableDefinition()
					.getDefaultDefinition(columnDefinition.getMappedProperty());
			if (defaultDefinition != null) {
				String result = defaultDefinition.getValue();
				Data data = defaultDefinition.getDataType().createData(result);
				args.add(data.getText(session, translator, configuration));
			} else {
				args.add("?");
			}
			columns.add(columnDefinition.getColumnName());
		}
		StringBuilder text = new StringBuilder();
		text.append(" (");
		text.append(CollectionUtils.join(columns, ","));
		text.append(") values (");
		text.append(CollectionUtils.join(args, ","));
		text.append(")");
		return text.toString();
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		ColumnDefinition[] columnDefinitions = translator.getColumns(null, configuration);
		for (ColumnDefinition columnDefinition : columnDefinitions) {
			if (excludeProperties.contains(columnDefinition.getMappedProperty())) {
				continue;
			}
			if (ignoreAutocrement && columnDefinition.isAutoIncrement()) {
				continue;
			}
			Object value = getPropertyValue(object, columnDefinition);
			if (value == null) {
				Generator generator = columnDefinition.getTableDefinition().getGenerator(columnDefinition.getMappedProperty());
				if (generator != null) {
					value = generator.postValue(session, configuration);
				}
			}
			if (value != null || columnDefinition.isNullable()) {
				parameterCollector.setParameter(value, columnDefinition.getJdbcType());
			} else {
				DefaultDefinition defaultDefinition = columnDefinition.getTableDefinition()
						.getDefaultDefinition(columnDefinition.getMappedProperty());
				if (defaultDefinition == null) {
					if (columnDefinition.getTableDefinition().isPrimaryKey(columnDefinition.getMappedProperty())) {
						throw new NullValueException("PrimaryKey '" + columnDefinition.getMappedProperty() + "' must not be null.");
					}
					throw new NullValueException("Property '" + columnDefinition.getMappedProperty() + "' must not be null.");
				}
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
