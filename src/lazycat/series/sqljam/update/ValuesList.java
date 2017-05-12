package lazycat.series.sqljam.update;

import java.util.ArrayList;
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
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.field.Data;
import lazycat.series.sqljam.generator.Generator;
import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.DefaultDefinition;

/**
 * ValuesList
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ValuesList implements Expression {

	public ValuesList(List<?> objectList) {
		Assert.isNull(objectList, "Null examples.");
		this.objectList = objectList;
	}

	private final List<?> objectList;
	private final Set<String> excludeProperties = new HashSet<String>();
	private final Map<String, Property> cache = new HashMap<String, Property>();
	private boolean ignoreAutocrement = true;

	public ValuesList ignoreAutocrement(boolean ignoreAutocrement) {
		this.ignoreAutocrement = ignoreAutocrement;
		return this;
	}

	public ValuesList excludeProperties(String... propertyNames) {
		if (propertyNames != null) {
			for (String propertyName : propertyNames) {
				excludeProperties.add(propertyName);
			}
		}
		return this;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		final List<String> columns = new ArrayList<String>();
		final List<String> args = new ArrayList<String>();
		ColumnDefinition[] definitions = translator.getColumns(null, configuration);
		for (ColumnDefinition columnDefinition : definitions) {
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
		text.append(CollectionUtils.join(columns, ", "));
		text.append(") values (");
		text.append(CollectionUtils.join(args, ", "));
		text.append(")");
		return text.toString();
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		for (int i = 0; i < objectList.size(); i++) {
			ColumnDefinition[] definitions = translator.getColumns(null, configuration);
			for (ColumnDefinition columnDefinition : definitions) {
				if (excludeProperties.contains(columnDefinition.getMappedProperty())) {
					continue;
				}
				if (ignoreAutocrement && columnDefinition.isAutoIncrement()) {
					continue;
				}
				Object value = getPropertyValue(objectList.get(i), columnDefinition);
				if (value == null) {
					Generator generator = columnDefinition.getGenerator();
					if (generator != null && generator.hasValue(session, configuration)) {
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
			parameterCollector.addBatch();
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
