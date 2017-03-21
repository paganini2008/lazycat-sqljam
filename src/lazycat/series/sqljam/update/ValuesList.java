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
import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.NullValueException;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.generator.IdentifierGenerator;
import lazycat.series.sqljam.relational.ColumnDefinition;

/**
 * ValuesList
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ValuesList implements Expression {

	public ValuesList(List<?> objectList) {
		Assert.isNull(objectList, "Null examples");
		this.objectList = objectList;
	}

	private final List<?> objectList;
	private final Set<String> excludeProperties = new HashSet<String>();
	private final Map<String, Property<?>> cache = new HashMap<String, Property<?>>();
	private boolean ignoreAutocrement = true;
	private boolean ignoreDefaultValue = true;

	public ValuesList ignoreAutocrement(boolean ignoreAutocrement) {
		this.ignoreAutocrement = ignoreAutocrement;
		return this;
	}

	public ValuesList ignoreDefaultValue(boolean ignoreDefaultValue) {
		this.ignoreDefaultValue = ignoreDefaultValue;
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

	public String getText(Translator translator, Configuration configuration) {
		final List<String> columns = new ArrayList<String>();
		final List<String> args = new ArrayList<String>();
		ColumnDefinition[] definitions = translator.getColumns(null, configuration.getMetaData());
		for (ColumnDefinition cd : definitions) {
			if (excludeProperties.contains(cd.getMappedProperty())) {
				continue;
			}
			if (ignoreAutocrement && cd.isAutoIncrement()) {
				continue;
			}
			if (ignoreDefaultValue && StringUtils.isNotBlank(cd.getDefaultValue())) {
				continue;
			}
			if (cd.getIdentifierGenerator() != null) {
				String text = cd.getIdentifierGenerator().getText(configuration.getFeature(), translator.getCurrentSession());
				if (StringUtils.isNotBlank(text)) {
					args.add(text);
					columns.add(cd.getColumnName());
				}
			} else {
				if (StringUtils.isNotBlank(cd.getInsertSql())) {
					args.add(cd.getInsertSql());
				} else {
					args.add("?");
				}
				columns.add(cd.getColumnName());
			}
		}
		StringBuilder text = new StringBuilder();
		text.append(" (");
		text.append(CollectionUtils.join(columns, ","));
		text.append(") values (");
		text.append(CollectionUtils.join(args, ","));
		text.append(")");
		return text.toString();
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		for (int i = 0; i < objectList.size(); i++) {
			ColumnDefinition[] definitions = translator.getColumns(null, configuration.getMetaData());
			for (ColumnDefinition cd : definitions) {
				if (excludeProperties.contains(cd.getMappedProperty())) {
					continue;
				}
				if (ignoreAutocrement && cd.isAutoIncrement()) {
					continue;
				}
				if (ignoreDefaultValue && StringUtils.isNotBlank(cd.getDefaultValue())) {
					continue;
				}
				Object value = null;
				IdentifierGenerator identifier = cd.getIdentifierGenerator();
				if (identifier != null && identifier.hasValue(configuration.getFeature(), translator.getCurrentSession())) {
					value = identifier.getValue(configuration.getFeature(), translator.getCurrentSession());
				} else {
					value = getPropertyValue(objectList.get(i), cd);
				}
				if (value != null || cd.isNullable()) {
					parameterCollector.setParameter(value, cd.getJdbcType());
				} else {
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
			parameterCollector.addBatch();
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
