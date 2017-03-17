package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * And
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class AndExpression extends LogicalExpression {

	private final Expression left;
	private final Expression right;

	public AndExpression(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}

	public String getText(Translator translator, Configuration configuration) {
		return configuration.getFeature().and(left.getText(translator, configuration), right.getText(translator, configuration));
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		left.setParameter(translator, parameterCollector, configuration);
		right.setParameter(translator, parameterCollector, configuration);
	}

}
