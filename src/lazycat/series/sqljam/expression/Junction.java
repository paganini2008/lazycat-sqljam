package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Junction
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Junction implements Expression {

	private final Expression left;
	private final Expression right;
	private final String s;

	public Junction(Expression left, Expression right) {
		this(left, right, ",");
	}

	public Junction(Expression left, Expression right, String s) {
		this.left = left;
		this.right = right;
		this.s = s;
	}

	public String getText(Translator translator, Configuration configuration) {
		StringBuilder text = new StringBuilder();
		text.append(left.getText(translator, configuration));
		text.append(s);
		text.append(right.getText(translator, configuration));
		return text.toString();
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		left.setParameter(translator, parameterCollector, configuration);
		right.setParameter(translator, parameterCollector, configuration);
	}
}
