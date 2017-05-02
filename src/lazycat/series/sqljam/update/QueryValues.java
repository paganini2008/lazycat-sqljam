package lazycat.series.sqljam.update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lazycat.series.collection.CollectionUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.query.From;
import lazycat.series.sqljam.relational.ColumnDefinition;

/**
 * QueryValues
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class QueryValues implements Expression {

	private final From source;
	private final Set<String> includedProperties = new HashSet<String>();

	public QueryValues(From source) {
		this.source = source;
	}

	public QueryValues includedProperties(String... propertyNames) {
		if (propertyNames != null) {
			includedProperties.addAll(Arrays.asList(propertyNames));
		}
		return this;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		final List<String> columns = new ArrayList<String>();
		ColumnDefinition[] definitions = translator.getColumns(null, configuration);
		for (ColumnDefinition definition : definitions) {
			if (includedProperties.contains(definition.getMappedProperty())) {
				columns.add(definition.getColumnName());
			}
		}
		StringBuilder text = new StringBuilder();
		if (columns.size() > 0) {
			text.append(" (");
			text.append(CollectionUtils.join(columns, ", "));
			text.append(")");
		}
		text.append(" (");
		text.append(source.getText(configuration));
		text.append(")");
		return text.toString();
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		source.setParameters(parameterCollector, configuration);
	}

}
