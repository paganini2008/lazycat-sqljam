package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * TextExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class TextExpression implements Expression {

	public TextExpression(String property, String text) {
		this(new StandardColumn(property), text);
	}

	public TextExpression(Field field, String text) {
		this.field = field;
		this.text = text;
	}

	private final Field field;
	private final String text;

	public String getText(Session session, Translator translator, Configuration configuration) {
		StringBuilder sql = new StringBuilder();
		sql.append(field.getText(session, translator, configuration));
		sql.append(text);
		return sql.toString();
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector,
			Configuration configuration) {
	}

}
