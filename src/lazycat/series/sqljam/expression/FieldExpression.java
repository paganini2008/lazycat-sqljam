package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * FieldExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class FieldExpression implements Expression {

	private final Field left;
	private final Field right;
	private final ComparisonOperator op;

	public FieldExpression(String property, String anotherProperty, ComparisonOperator op) {
		this(new StandardColumn(property), anotherProperty, op);
	}

	public FieldExpression(Field field, String property, ComparisonOperator op) {
		this(field, new StandardColumn(property), op);
	}

	public FieldExpression(String property, Field field, ComparisonOperator op) {
		this(new StandardColumn(property), field, op);
	}

	public FieldExpression(Field left, Field right, ComparisonOperator op) {
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
	}

}
