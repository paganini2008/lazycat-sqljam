package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * Wrap the expression. Start by '(' and end by ')'
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class WrapExpression extends LogicalExpression {

	public WrapExpression(Expression expression) {
		this.expression = expression;
	}

	private final Expression expression;

	public String getText(Session session, Translator translator, Configuration configuration) {
		return configuration.getJdbcAdmin().getFeature().wrap(expression.getText(session, translator, configuration));
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		expression.setParameter(session, translator, parameterCollector, configuration);
	}

}
