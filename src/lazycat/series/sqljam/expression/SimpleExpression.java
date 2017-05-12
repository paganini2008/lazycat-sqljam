package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.field.Field;
import lazycat.series.sqljam.field.StandardColumn;

/**
 * SimpleExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SimpleExpression implements Expression {

	private final Field left;
	private final Expression right;
	private final String op;

	public SimpleExpression(String property, Expression right, String op) {
		this(new StandardColumn(property), right, op);
	}

	public SimpleExpression(Field left, Expression right, String op) {
		this.left = left;
		this.right = right;
		this.op = op;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		StringBuilder content = new StringBuilder();
		content.append(left.getText(session, translator, configuration));
		content.append(op);
		content.append(right.getText(session, translator, configuration));
		return content.toString();
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		right.setParameter(session, translator, parameterCollector, configuration);
	}

}
