package lazycat.series.sqljam.expression;

import lazycat.series.jdbc.JdbcType;

/**
 * SqlParameterExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SqlParameterExpression extends ComparisonExpression {

	public SqlParameterExpression(String property, Object parameter) {
		this(property, parameter, ComparisonOperator.EQ);
	}

	public SqlParameterExpression(String property, Object parameter, ComparisonOperator op) {
		this(property, parameter, JdbcType.OTHER, op);
	}

	public SqlParameterExpression(String property, Object parameter, JdbcType jdbcType) {
		this(new Column(property), parameter, jdbcType, ComparisonOperator.EQ);
	}

	public SqlParameterExpression(String property, Object parameter, JdbcType jdbcType, ComparisonOperator op) {
		this(new Column(property), parameter, jdbcType, op);
	}

	public SqlParameterExpression(Expression expression, Object parameter, JdbcType jdbcType) {
		this(expression, parameter, jdbcType, ComparisonOperator.EQ);
	}

	public SqlParameterExpression(Expression expression, Object parameter) {
		this(expression, parameter, JdbcType.OTHER, ComparisonOperator.EQ);
	}

	public SqlParameterExpression(Expression expression, Object parameter, ComparisonOperator op) {
		this(expression, parameter, JdbcType.OTHER, op);
	}

	public SqlParameterExpression(Expression expression, Object parameter, JdbcType jdbcType, ComparisonOperator op) {
		super(expression, new Parameter(parameter, jdbcType), op);
	}

}
