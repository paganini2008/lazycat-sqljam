package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.field.Field;
import lazycat.series.sqljam.field.StandardColumn;

/**
 * ComparisonExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class BooleanExpression extends LogicalExpression {

	private final Field left;
	private final Expression right;
	private final BooleanOperator op;

	public BooleanExpression(String property, Expression expression, BooleanOperator op) {
		this(new StandardColumn(property), expression, op);
	}

	public BooleanExpression(Field left, Expression right, BooleanOperator op) {
		this.left = left;
		this.right = right;
		this.op = op;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return op.getText(configuration.getJdbcAdmin().getFeature(), left.getText(session, translator, configuration),
				right.getText(session, translator, configuration));
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		right.setParameter(session, translator, parameterCollector, configuration);
	}

}
