package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.query.From;

/**
 * Exists
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ExistsExpression implements Expression {

	public ExistsExpression(From query) {
		this.query = query;
	}

	private final From query;

	public String getText(Session session, Translator translator, Configuration configuration) {
		return configuration.getJdbcAdmin().getFeature().exists(query.getText(configuration));
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector,
			Configuration configuration) {
		query.setParameters(parameterCollector, configuration);
	}

}
