package lazycat.series.sqljam.field;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * NamingFunction
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class NestedFunction extends MathColumn {

	private final String name;
	private final Field expression;

	public NestedFunction(String name, Field expression) {
		this.name = name;
		this.expression = expression;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return name + "(" + expression.getText(session, translator, configuration) + ")";
	}

}
