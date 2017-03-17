package lazycat.series.sqljam.expression;

/**
 * NumberExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class NumberExpression extends ArithmeticExpression {

	public NumberExpression(String property, Number number, ArithmeticOperator op) {
		super(property, new Text(number), op);
	}

	public NumberExpression(Expression expression, Number number, ArithmeticOperator op) {
		super(expression, new Text(number), op);
	}

}
