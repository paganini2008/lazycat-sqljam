package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * True
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class TrueExpression implements Expression {

	public String getText(Translator translator, Configuration configuration) {
		return "1=1";
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
