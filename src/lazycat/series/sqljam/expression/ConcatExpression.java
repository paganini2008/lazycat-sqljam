package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * ConcatExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ConcatExpression implements Expression {

	public ConcatExpression(String property, String anotherProperty) {
		this(property, new Column(property));
	}

	public ConcatExpression(String property, Expression expression) {
		this(new Column(property), expression);
	}

	public ConcatExpression(Expression expression, String property) {
		this(expression, new Column(property));
	}

	public ConcatExpression(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}

	private final Expression left;
	private final Expression right;

	public String getText(Translator translator, Configuration configuration) {
		String a = left.getText(translator, configuration);
		String b = right.getText(translator, configuration);
		return configuration.getFeature().concat(a, b);
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		left.setParameter(translator, parameterCollector, configuration);
		right.setParameter(translator, parameterCollector, configuration);
	}

}
