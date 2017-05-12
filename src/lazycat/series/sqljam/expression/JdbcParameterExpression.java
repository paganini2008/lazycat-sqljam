package lazycat.series.sqljam.expression;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.field.Field;
import lazycat.series.sqljam.field.StandardColumn;

/**
 * JdbcParameterExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class JdbcParameterExpression extends BooleanExpression {

	public JdbcParameterExpression(String property, Object parameter, BooleanOperator op) {
		this(property, parameter, JdbcType.OTHER, op);
	}

	public JdbcParameterExpression(String property, Object parameter, JdbcType jdbcType, BooleanOperator op) {
		this(new StandardColumn(property), parameter, jdbcType, op);
	}

	public JdbcParameterExpression(Field field, Object parameter, BooleanOperator op) {
		this(field, parameter, JdbcType.OTHER, op);
	}

	public JdbcParameterExpression(Field field, Object parameter, JdbcType jdbcType, BooleanOperator op) {
		super(field, new JdbcParameter(parameter, jdbcType), op);
	}

}
