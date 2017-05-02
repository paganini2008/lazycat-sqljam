package lazycat.series.sqljam.expression;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * BetweenExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class BetweenExpression implements Expression {

	private final Field field;
	private final Expression lower;
	private final Expression high;

	public BetweenExpression(String propertyName, Object lower, Object high) {
		this(new StandardColumn(propertyName), lower, high);
	}

	public BetweenExpression(Field field, Object lower, Object high) {
		this(field, lower, high, JdbcType.OTHER);
	}

	public BetweenExpression(String propertyName, Object lower, Object high, JdbcType jdbcType) {
		this(new StandardColumn(propertyName), lower, high, jdbcType);
	}

	public BetweenExpression(Field field, Object lower, Object high, JdbcType jdbcType) {
		this(field, new JdbcParameter(lower, jdbcType), new JdbcParameter(high, jdbcType));
	}

	public BetweenExpression(Field field, Expression lower, Expression high) {
		this.field = field;
		this.lower = lower;
		this.high = high;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return configuration.getJdbcAdmin().getFeature().between(field.getText(session, translator, configuration),
				lower.getText(session, translator, configuration), high.getText(session, translator, configuration));
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector,
			Configuration configuration) {
		lower.setParameter(session, translator, parameterCollector, configuration);
		high.setParameter(session, translator, parameterCollector, configuration);
	}

}
