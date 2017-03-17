package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Text
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Text implements Expression {

	private Object object;

	public Text(Object object) {
		this.object = object;
	}

	public String getText(Translator translator, Configuration configuration) {
		return object != null ? object.toString() : "";
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
