package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Asc
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Asc implements Expression {

	private final Expression expression;

	public Asc(String property) {
		this.expression = new Column(property);
	}

	public Asc(Expression expression) {
		this.expression = expression;
	}

	public String getText(Translator translator, Configuration configuration) {
		return configuration.getFeature().asc(expression.getText(translator, configuration));
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		expression.setParameter(translator, parameterCollector, configuration);
	}

}
