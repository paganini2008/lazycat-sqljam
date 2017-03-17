package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Record number
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class RowCount implements Expression {

	public static final RowCount ONE = new RowCount(Label.ONE);

	private final Expression expression;

	public RowCount() {
		this("*");
	}

	public RowCount(String property) {
		this(new Column(property));
	}

	public RowCount(Expression expression) {
		this.expression = expression;
	}

	public String getText(Translator translator, Configuration configuration) {
		String s = expression.getText(translator, configuration);
		return configuration.getFeature().count(s);
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
