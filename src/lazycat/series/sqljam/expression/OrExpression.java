package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * OrExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class OrExpression extends LogicalExpression {

	private final Expression left;
	private final Expression right;

	public OrExpression(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return configuration.getJdbcAdmin().getFeature().or(left.getText(session, translator, configuration),
				right.getText(session, translator, configuration));
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		left.setParameter(session, translator, parameterCollector, configuration);
		right.setParameter(session, translator, parameterCollector, configuration);
	}

	public static LogicalExpression create(Expression... expressions) {
		LogicalExpression or = null;
		for (Expression expression : expressions) {
			if (or == null) {
				or = new AndExpression(expression);
			} else {
				or = or.or(expression);
			}
		}
		return or;
	}

}
