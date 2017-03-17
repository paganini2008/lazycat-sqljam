package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * ComparisonExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ComparisonExpression implements Expression {

	private final Expression left;
	private final Expression right;
	private final ComparisonOperator op;
	
	public ComparisonExpression(String property, String anotherProperty, ComparisonOperator op) {
		this(property, new Column(anotherProperty), op);
	}

	public ComparisonExpression(Expression expression, String property, ComparisonOperator op) {
		this(expression, new Column(property), op);
	}

	public ComparisonExpression(String property, Expression expression, ComparisonOperator op) {
		this(new Column(property), expression, op);
	}

	public ComparisonExpression(Expression left, Expression right, ComparisonOperator op) {
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
