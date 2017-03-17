package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * SimpleExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SimpleExpression implements Expression {

	private final Expression left;
	private final Expression right;
	private final String op;

	public SimpleExpression(String property, String anotherProperty, String op) {
		this(property, new Column(anotherProperty), op);
	}

	public SimpleExpression(Expression right, String property, String op) {
		this(right, new Column(property), op);
	}

	public SimpleExpression(String property, Expression right, String op) {
		this(new Column(property), right, op);
	}

	public SimpleExpression(Expression left, Expression right, String op) {
		this.left = left;
		this.right = right;
		this.op = op;
	}

	public String getText(Translator translator, Configuration configuration) {
		StringBuilder content = new StringBuilder();
		content.append(left.getText(translator, configuration));
		content.append(op);
		content.append(right.getText(translator, configuration));
		return content.toString();
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		left.setParameter(translator, parameterCollector, configuration);
		right.setParameter(translator, parameterCollector, configuration);
	}

}
