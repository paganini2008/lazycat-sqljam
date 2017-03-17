package lazycat.series.sqljam.expression;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * InExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class InExpression implements Expression {

	private final Expression expression;
	private final Expression in;

	public InExpression(Expression expression, Object[] parameters) {
		this(expression, parameters, JdbcType.OTHER);
	}

	public InExpression(String propertyName, Object[] parameters) {
		this(propertyName, parameters, JdbcType.OTHER);
	}

	public InExpression(String propertyName, Object[] parameters, JdbcType jdbcType) {
		this(new Column(propertyName), parameters, jdbcType);
	}

	public InExpression(Expression expression, Object[] parameters, JdbcType jdbcType) {
		this(expression, ParameterList.create(parameters, jdbcType));
	}

	public InExpression(String propertyName, Expression in) {
		this(new Column(propertyName), in);
	}

	public InExpression(Expression expression, Expression in) {
		this.expression = expression;
		this.in = in;
	}

	public String getText(Translator translator, Configuration configuration) {
		return configuration.getFeature().in(expression.getText(translator, configuration), in.getText(translator, configuration));
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		expression.setParameter(translator, parameterCollector, configuration);
		in.setParameter(translator, parameterCollector, configuration);
	}

}
