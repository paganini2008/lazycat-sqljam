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
import lazycat.series.sqljam.generator.IdentifierGenerator;
import lazycat.series.sqljam.relational.ColumnDefinition;

/**
 * Values
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Values implements Expression {

	private final Object object;

	public Values(Object object) {
		Assert.isNull(object, "Null example");
		this.object = object;
	}

	private final Set<String> excludeProperties = new HashSet<String>();
	private final Map<String, Property<?>> cache = new HashMap<String, Property<?>>();
	private boolean ignoreAutocrement = true;
	private boolean ignoreDefaultValue = true;

	public Values ignoreAutocrement(boolean ignoreAutocrement) {
		this.ignoreAutocrement = ignoreAutocrement;
		return this;
	}

	public Values ignoreDefaultValue(boolean ignoreDefaultValue) {
		this.ignoreDefaultValue = ignoreDefaultValue;
		return this;
	}

	public Values excludeProperties(String... propertyNames) {
		if (propertyNames != null) {
			excludeProperties.addAll(Arrays.asList(propertyNames));
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
				Object value = getPropertyValue(object, cd);
				if (value != null || cd.isNullable()) {
					args.add("?");
				} else {
					if (StringUtils.isNotBlank(cd.getInsertSql())) {
						args.add(cd.getInsertSql());
					}
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
				value = getPropertyValue(object, cd);
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
