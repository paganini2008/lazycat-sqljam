package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Not Expression
 * @author Fred Feng
 * @version 1.0
 */
public class NotExpression extends LogicalExpression {

	private final Expression expression;

	public NotExpression(Expression expression) {
		this.expression = expression;
	}

	public String getText(Translator translator, Configuration configuration) {
		return configuration.getFeature().not(expression.getText(translator, configuration)).toString();
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		expression.setParameter(translator, parameterCollector, configuration);
	}

}
