package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * AndExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class AndExpression extends LogicalExpression {

	private final Expression left;
	private final Expression right;

	public AndExpression(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		String leftContent = left.getText(session, translator, configuration);
		String rightContent = right.getText(session, translator, configuration);
		return configuration.getJdbcAdmin().getFeature().and(leftContent, rightContent);
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector,
			Configuration configuration) {
		left.setParameter(session, translator, parameterCollector, configuration);
		right.setParameter(session, translator, parameterCollector, configuration);
	}

}
