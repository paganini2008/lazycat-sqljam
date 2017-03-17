package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Using
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Using implements Expression {

	private final String propertyName;

	public Using(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getText(Translator translator, Configuration configuration) {
		String columnName = translator.getColumnName(null, propertyName, configuration.getMetaData());
		return configuration.getFeature().using(columnName);
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
