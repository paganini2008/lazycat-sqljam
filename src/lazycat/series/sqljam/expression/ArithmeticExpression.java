package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * ArithmeticExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ArithmeticExpression implements Expression {

	private final Expression left;
	private final Expression right;
	private final ArithmeticOperator op;
	
	public ArithmeticExpression(String property, String anotherProperty, ArithmeticOperator op) {
		this(property, new Column(property), op);
	}

	public ArithmeticExpression(String property, Expression expression, ArithmeticOperator op) {
		this(new Column(property), expression, op);
	}

	public ArithmeticExpression(Expression expression, String property, ArithmeticOperator op) {
		this(expression, new Column(property), op);
	}

	public ArithmeticExpression(Expression left, Expression right, ArithmeticOperator op) {
		this.left = left;
		this.right = right;
		this.op = op;
	}

	public String getText(Translator translator, Configuration configuration) {
		return op.getText(configuration.getFeature(), left.getText(translator, configuration), right.getText(translator, configuration));
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		left.setParameter(translator, parameterCollector, configuration);
		right.setParameter(translator, parameterCollector, configuration);
	}

}
