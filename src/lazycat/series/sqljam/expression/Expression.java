package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * Expression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Expression {

	String getText(Session session, Translator translator, Configuration configuration);

	void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration);
}
