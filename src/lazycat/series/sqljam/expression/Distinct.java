package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Distinct
 * 
 * @author Fred Feng
 * @version 1.0
 */
@Field
public class Distinct implements Expression {

	private final Expression expression;

	public Distinct(String property) {
		this.expression = new Column(property);
	}

	public Distinct(Expression expression) {
		this.expression = expression;
	}

	public String getText(Translator translator, Configuration configuration) {
		String sql = expression.getText(translator, configuration);
		return configuration.getFeature().distinct(sql);
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		expression.setParameter(translator, parameterCollector, configuration);
	}

}
