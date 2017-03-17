package lazycat.series.sqljam.expression;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * between
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class BetweenExpression implements Expression {

	private final Expression expression;
	private final Expression lower;
	private final Expression high;

	public BetweenExpression(String propertyName, Object lower, Object high) {
		this(new Column(propertyName), lower, high);
	}

	public BetweenExpression(Expression expression, Object lower, Object high) {
		this(expression, lower, high, JdbcType.OTHER);
	}

	public BetweenExpression(String propertyName, Object lower, Object high, JdbcType jdbcType) {
		this(new Column(propertyName), lower, high, jdbcType);
	}

	public BetweenExpression(Expression expression, Object lower, Object high, JdbcType jdbcType) {
		this(expression, new Parameter(lower, jdbcType), new Parameter(high, jdbcType));
	}

	public BetweenExpression(Expression expression, Expression lower, Expression high) {
		this.expression = expression;
		this.lower = lower;
		this.high = high;
	}

	public String getText(Translator translator, Configuration configuration) {
		return configuration.getFeature().between(expression.getText(translator, configuration), lower.getText(translator, configuration),
				high.getText(translator, configuration));
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		expression.setParameter(translator, parameterCollector, configuration);
		lower.setParameter(translator, parameterCollector, configuration);
		high.setParameter(translator, parameterCollector, configuration);
	}

}
