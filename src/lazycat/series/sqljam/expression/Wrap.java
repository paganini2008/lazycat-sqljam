package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Wrap the expression. Start by '(' and end by ')'
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Wrap extends LogicalExpression {

	public Wrap(Expression expression) {
		this.expression = expression;
	}

	private final Expression expression;

	public String getText(Translator translator, Configuration configuration) {
		return configuration.getFeature().wrap(expression.getText(translator, configuration));
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		expression.setParameter(translator, parameterCollector, configuration);
	}

}
