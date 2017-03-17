package lazycat.series.sqljam.expression;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.LikeExpression.MatchMode;

public class OrFilter extends LogicalExpression {

	public OrFilter(Expression expression) {
		delegate = expression;
	}

	private Expression delegate;

	public OrFilter eq(String propertyName, Object parameter, JdbcType jdbcType) {
		return or(new SqlParameterExpression(propertyName, parameter, jdbcType, ComparisonOperator.EQ));
	}

	public OrFilter ne(String propertyName, Object parameter, JdbcType jdbcType) {
		return or(new SqlParameterExpression(propertyName, parameter, jdbcType, ComparisonOperator.NE));
	}

	public OrFilter gt(String propertyName, Object parameter, JdbcType jdbcType) {
		return or(new SqlParameterExpression(propertyName, parameter, jdbcType, ComparisonOperator.GT));
	}

	public OrFilter lt(String propertyName, Object parameter, JdbcType jdbcType) {
		return or(new SqlParameterExpression(propertyName, parameter, jdbcType, ComparisonOperator.LT));
	}

	public OrFilter gte(String propertyName, Object parameter, JdbcType jdbcType) {
		return or(new SqlParameterExpression(propertyName, parameter, jdbcType, ComparisonOperator.GTE));
	}

	public OrFilter lte(String propertyName, Object parameter, JdbcType jdbcType) {
		return or(new SqlParameterExpression(propertyName, parameter, jdbcType, ComparisonOperator.LTE));
	}

	public OrFilter between(String propertyName, Object lower, Object high, JdbcType jdbcType) {
		return or(new BetweenExpression(propertyName, lower, high, jdbcType));
	}

	public OrFilter in(String propertyName, Object[] arguments, JdbcType jdbcType) {
		return or(new InExpression(propertyName, arguments, jdbcType));
	}

	public OrFilter like(String propertyName, String like, MatchMode matchMode) {
		return or(new LikeExpression(propertyName, like, matchMode));
	}

	public OrFilter isNull(String propertyName) {
		return or(new NullableExpression(propertyName, true));
	}

	public OrFilter isNotNull(String propertyName) {
		return or(new NullableExpression(propertyName, false));
	}

	public OrFilter or(Expression expression) {
		if (delegate instanceof LogicalExpression) {
			delegate = ((LogicalExpression) expression).or(expression);
		} else {
			delegate = new OrExpression(delegate, expression);
		}
		return this;
	}

	public String getText(Translator translator, Configuration configuration) {
		return delegate.getText(translator, configuration);
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		delegate.setParameter(translator, parameterCollector, configuration);
	}

	public static OrFilter newFilter(Expression expression) {
		return new OrFilter(expression);
	}

}
