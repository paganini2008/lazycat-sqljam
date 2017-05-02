package lazycat.series.sqljam.expression;

import lazycat.series.jdbc.JdbcType;

/**
 * JdbcParameterExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class JdbcParameterExpression extends ComparisonExpression {

	public JdbcParameterExpression(String property, Object parameter, ComparisonOperator op) {
		this(property, parameter, JdbcType.OTHER, op);
	}

	public JdbcParameterExpression(String property, Object parameter, JdbcType jdbcType, ComparisonOperator op) {
		this(new StandardColumn(property), parameter, jdbcType, op);
	}

	public JdbcParameterExpression(Field field, Object parameter, ComparisonOperator op) {
		this(field, parameter, JdbcType.OTHER, op);
	}

	public JdbcParameterExpression(Field field, Object parameter, JdbcType jdbcType, ComparisonOperator op) {
		super(field, new JdbcParameter(parameter, jdbcType), op);
	}

}
