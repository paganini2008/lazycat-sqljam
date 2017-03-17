package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Expression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Expression {

	String getText(Translator translator, Configuration configuration);

	void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration);
}
