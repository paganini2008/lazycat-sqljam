package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * NullableExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class NullableExpression implements Expression {

	private final Field field;
	private final boolean yes;

	public NullableExpression(String propertyName, boolean yes) {
		this(new StandardColumn(propertyName), yes);
	}

	public NullableExpression(Field field, boolean yes) {
		this.field = field;
		this.yes = yes;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return configuration.getJdbcAdmin().getFeature().nullable(field.getText(session, translator, configuration), yes);
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}
}
