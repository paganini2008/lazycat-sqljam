package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * TextExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class TextExpression implements Expression {

	public TextExpression(String property, String text) {
		this(new Column(property), text);
	}

	public TextExpression(Expression expression, String text) {
		this.expression = expression;
		this.text = text;
	}

	private final Expression expression;
	private final String text;

	public String getText(Translator translator, Configuration configuration) {
		StringBuilder sql = new StringBuilder();
		sql.append(expression.getText(translator, configuration));
		sql.append(text);
		return sql.toString();
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		expression.setParameter(translator, parameterCollector, configuration);
	}

}
