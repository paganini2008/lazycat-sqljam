package lazycat.series.sqljam.expression;

import lazycat.series.lang.Assert;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Label
 * 
 * @author Fred Feng
 * @version 1.0
 */
@Field
public class Label implements Expression {

	public static final Label ONE = new Label(1);

	private final String expression;

	public Label(Object object) {
		Assert.isNull(object, "Null label");
		this.expression = object.toString();
	}

	public String getText(Translator translator, Configuration configuration) {
		return expression;
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
