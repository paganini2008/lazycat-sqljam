package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * ComparisonExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ComparisonExpression implements Expression {

	private final Field left;
	private final Expression right;
	private final ComparisonOperator op;

	public ComparisonExpression(String property, Expression expression, ComparisonOperator op) {
		this(new StandardColumn(property), expression, op);
	}

	public ComparisonExpression(Field left, Expression right, ComparisonOperator op) {
		this.left = left;
		this.right = right;
		this.op = op;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return op.getText(configuration.getJdbcAdmin().getFeature(), left.getText(session, translator, configuration),
				right.getText(session, translator, configuration));
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector,
			Configuration configuration) {
		right.setParameter(session, translator, parameterCollector, configuration);
	}

}
