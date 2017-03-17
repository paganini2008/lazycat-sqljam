package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.query.From;

/**
 * QueryExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class QueryExpression implements Expression {

	private final From query;

	public QueryExpression(From query) {
		this.query = query;
	}

	public String getText(Translator translator, Configuration configuration) {
		return "(" + query.getText(configuration) + ")";
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		query.setParameters(parameterCollector, configuration);
	}

}
