package lazycat.series.sqljam.expression;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * InExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class InExpression implements Expression {

	private final Field field;
	private final Expression in;

	public InExpression(Field field, Object[] parameters) {
		this(field, parameters, JdbcType.OTHER);
	}

	public InExpression(String propertyName, Object[] parameters) {
		this(propertyName, parameters, JdbcType.OTHER);
	}

	public InExpression(String propertyName, Object[] parameters, JdbcType jdbcType) {
		this(new StandardColumn(propertyName), parameters, jdbcType);
	}

	public InExpression(Field expression, Object[] parameters, JdbcType jdbcType) {
		this(expression, JdbcParameterList.create(parameters, jdbcType));
	}

	public InExpression(String propertyName, Expression in) {
		this(new StandardColumn(propertyName), in);
	}

	public InExpression(Field field, Expression in) {
		this.field = field;
		this.in = in;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return configuration.getJdbcAdmin().getFeature().in(field.getText(session, translator, configuration),
				in.getText(session, translator, configuration));
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		in.setParameter(session, translator, parameterCollector, configuration);
	}

}
