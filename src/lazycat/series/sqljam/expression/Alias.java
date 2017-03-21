package lazycat.series.sqljam.expression;

import lazycat.series.lang.Assert;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Alias
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Alias implements Expression {

	private final Expression expression;
	private final String alias;

	public Alias(String property, String alias) {
		this(new Column(property), alias);
	}

	public Alias(Expression expression, String alias) {
		Assert.hasNoText(alias, "Undefined alias.");
		this.expression = expression;
		this.alias = alias;
	}

	public String getText(Translator translator, Configuration configuration) {
		return configuration.getFeature().columnAs(expression.getText(translator, configuration), alias);
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		expression.setParameter(translator, parameterCollector, configuration);
	}

}
