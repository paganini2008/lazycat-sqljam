package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Into
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Into implements Expression {

	private final String tableName;

	public Into(String tableName) {
		this.tableName = tableName;
	}

	public String getText(Translator translator, Configuration configuration) {
		return configuration.getFeature().into(tableName);
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
