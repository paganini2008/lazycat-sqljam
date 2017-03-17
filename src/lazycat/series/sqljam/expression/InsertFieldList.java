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
import lazycat.series.sqljam.relational.ColumnDefinition;

/**
 * Insert clause fields
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class InsertFieldList implements Expression {

	public InsertFieldList() {
	}

	private final Set<String> excludeProperties = new HashSet<String>();
	private boolean ignoreAutocrement = true;
	private boolean ignoreDefaultValue = true;

	public InsertFieldList setIgnoreAutocrement(boolean ignoreAutocrement) {
		this.ignoreAutocrement = ignoreAutocrement;
		return this;
	}

	public InsertFieldList setIgnoreDefaultValue(boolean ignoreDefaultValue) {
		this.ignoreDefaultValue = ignoreDefaultValue;
		return this;
	}

	public InsertFieldList excludeProperties(String... propertyNames) {
		if (propertyNames != null) {
			for (String propertyName : propertyNames) {
				excludeProperties.add(propertyName);
			}
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
			if (ignoreAutocrement && cd.isAutoIncrement()) {
				continue;
			}
			if (ignoreDefaultValue && StringUtils.isNotBlank(cd.getDefaultValue())) {
				continue;
			}
			list.add(cd.getColumnName());
		}
		return CollectionUtils.join(list, ",");
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
