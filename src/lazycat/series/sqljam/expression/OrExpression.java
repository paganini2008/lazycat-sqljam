package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

public class OrExpression extends LogicalExpression {

	private final Expression left;
	private final Expression right;

	public OrExpression(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}

	public String getText(Translator translator, Configuration configuration) {
		return configuration.getFeature().or(left.getText(translator, configuration), right.getText(translator, configuration));
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		left.setParameter(translator, parameterCollector, configuration);
		right.setParameter(translator, parameterCollector, configuration);
	}

}
