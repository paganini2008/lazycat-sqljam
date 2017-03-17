package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Nullable Expression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class NullableExpression implements Expression {

	private final Expression expression;
	private final boolean yes;

	public NullableExpression(String propertyName, boolean yes) {
		this(new Column(propertyName), yes);
	}

	public NullableExpression(Expression expression, boolean yes) {
		this.expression = expression;
		this.yes = yes;
	}

	public String getText(Translator translator, Configuration configuration) {
		return configuration.getFeature().nullable(expression.getText(translator, configuration), yes);
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		expression.setParameter(translator, parameterCollector, configuration);
	}
}
