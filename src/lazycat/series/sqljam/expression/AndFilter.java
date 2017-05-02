package lazycat.series.sqljam.expression;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.LikeExpression.MatchMode;

/**
 * AndFilter
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class AndFilter extends LogicalExpression {

	public AndFilter(Expression left, Expression right) {
		delegate = new AndExpression(left, right);
	}

	private LogicalExpression delegate;

	public AndFilter eq(String propertyName, Object parameter, JdbcType jdbcType) {
		return and(new JdbcParameterExpression(propertyName, parameter, jdbcType, ComparisonOperator.EQ));
	}

	public AndFilter ne(String propertyName, Object parameter, JdbcType jdbcType) {
		return and(new JdbcParameterExpression(propertyName, parameter, jdbcType, ComparisonOperator.NE));
	}

	public AndFilter gt(String propertyName, Object parameter, JdbcType jdbcType) {
		return and(new JdbcParameterExpression(propertyName, parameter, jdbcType, ComparisonOperator.GT));
	}

	public AndFilter lt(String propertyName, Object parameter, JdbcType jdbcType) {
		return and(new JdbcParameterExpression(propertyName, parameter, jdbcType, ComparisonOperator.LT));
	}

	public AndFilter gte(String propertyName, Object parameter, JdbcType jdbcType) {
		return and(new JdbcParameterExpression(propertyName, parameter, jdbcType, ComparisonOperator.GTE));
	}

	public AndFilter lte(String propertyName, Object parameter, JdbcType jdbcType) {
		return and(new JdbcParameterExpression(propertyName, parameter, jdbcType, ComparisonOperator.LTE));
	}

	public AndFilter between(String propertyName, Object lower, Object high, JdbcType jdbcType) {
		return and(new BetweenExpression(propertyName, lower, high, jdbcType));
	}

	public AndFilter in(String propertyName, Object[] arguments, JdbcType jdbcType) {
		return and(new InExpression(propertyName, arguments, jdbcType));
	}

	public AndFilter like(String propertyName, String like, MatchMode matchMode) {
		return and(new LikeExpression(propertyName, like, matchMode));
	}

	public AndFilter isNull(String propertyName) {
		return and(new NullableExpression(propertyName, true));
	}

	public AndFilter isNotNull(String propertyName) {
		return and(new NullableExpression(propertyName, false));
	}

	public AndFilter and(Expression expression) {
		delegate = new AndExpression(delegate, expression);
		return this;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return delegate.getText(session, translator, configuration);
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		delegate.setParameter(session, translator, parameterCollector, configuration);
	}

	public static AndFilter newFilter(Expression expression) {
		return new AndFilter(Expressions.TRUE, expression);
	}

}
