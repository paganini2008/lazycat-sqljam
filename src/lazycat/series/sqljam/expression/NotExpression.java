package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * Not Expression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class NotExpression extends LogicalExpression {

	private final Expression expression;

	public NotExpression(Expression expression) {
		this.expression = expression;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return configuration.getJdbcAdmin().getFeature().not(expression.getText(session, translator, configuration));
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		expression.setParameter(session, translator, parameterCollector, configuration);
	}

}
