package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.expression.LikeExpression.MatchMode;
import lazycat.series.sqljam.query.From;

/**
 * Expression Utils
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Expressions {

	public static final TrueExpression TRUE = new TrueExpression();

	public static final FalseExpression FALSE = new FalseExpression();

	public static LogicalExpression and(Expression expression, Expression otherExpression) {
		return new AndExpression(expression, otherExpression);
	}

	public static LogicalExpression or(Expression expression, Expression otherExpression) {
		return new OrExpression(expression, otherExpression);
	}

	public static LogicalExpression not(Expression expression) {
		return new NotExpression(expression);
	}

	public static LogicalExpression wrap(Expression expression) {
		return new Wrap(expression);
	}

	public static Expression eq(String property, Object parameter) {
		return new JdbcParameterExpression(property, parameter, ComparisonOperator.EQ);
	}

	public static Expression eq(Field field, Object parameter) {
		return new JdbcParameterExpression(field, parameter, ComparisonOperator.EQ);
	}

	public static Expression eq(String property, From query) {
		return new JdbcParameterExpression(property, query, ComparisonOperator.EQ);
	}

	public static Expression ne(String property, Object parameter) {
		return new JdbcParameterExpression(property, parameter, ComparisonOperator.NE);
	}

	public static Expression ne(String property, From query) {
		return new JdbcParameterExpression(property, query, ComparisonOperator.NE);
	}

	public static Expression neAny(String property, From query) {
		return new JdbcParameterExpression(property, query, ComparisonOperator.NE_ANY);
	}

	public static Expression neAll(String property, From query) {
		return new JdbcParameterExpression(property, query, ComparisonOperator.NE_ALL);
	}

	public static Expression lt(String property, Object parameter) {
		return new JdbcParameterExpression(property, parameter, ComparisonOperator.LT);
	}

	public static Expression lt(String property, From query) {
		return new JdbcParameterExpression(property, query, ComparisonOperator.LT);
	}

	public static Expression ltAny(String property, From query) {
		return new JdbcParameterExpression(property, query, ComparisonOperator.LT_ANY);
	}

	public static Expression ltAll(String property, From query) {
		return new JdbcParameterExpression(property, query, ComparisonOperator.LT_ALL);
	}

	public static Expression gt(String property, Object parameter) {
		return new JdbcParameterExpression(property, parameter, ComparisonOperator.GT);
	}

	public static Expression gt(String property, From query) {
		return new JdbcParameterExpression(property, query, ComparisonOperator.GT);
	}

	public static Expression gtAny(String property, From query) {
		return new JdbcParameterExpression(property, query, ComparisonOperator.GT_ANY);
	}

	public static Expression gtAll(String property, From query) {
		return new JdbcParameterExpression(property, query, ComparisonOperator.GT_ALL);
	}

	public static Expression lte(String property, Object parameter) {
		return new JdbcParameterExpression(property, parameter, ComparisonOperator.LTE);
	}

	public static Expression lte(String property, From query) {
		return new JdbcParameterExpression(property, query, ComparisonOperator.LTE);
	}

	public static Expression lteAny(String property, From query) {
		return new JdbcParameterExpression(property, query, ComparisonOperator.LTE_ANY);
	}

	public static Expression lteAll(String property, From query) {
		return new JdbcParameterExpression(property, query, ComparisonOperator.LTE_ALL);
	}

	public static Expression gte(String property, Object parameter) {
		return new JdbcParameterExpression(property, parameter, ComparisonOperator.GTE);
	}

	public static Expression gte(String property, From query) {
		return new JdbcParameterExpression(property, query(query), ComparisonOperator.GTE);
	}

	public static Expression gteAny(String property, From query) {
		return new JdbcParameterExpression(property, query(query), ComparisonOperator.GTE_ANY);
	}

	public static Expression gteAll(String property, From query) {
		return new JdbcParameterExpression(property, query(query), ComparisonOperator.GTE_ALL);
	}

	public static Expression eqProperty(String property, String anotherProperty) {
		return new FieldExpression(property, anotherProperty, ComparisonOperator.EQ);
	}

	public static Expression neProperty(String property, String anotherProperty) {
		return new FieldExpression(property, anotherProperty, ComparisonOperator.NE);
	}

	public static Expression ltProperty(String property, String anotherProperty) {
		return new FieldExpression(property, anotherProperty, ComparisonOperator.LT);
	}

	public static Expression gtProperty(String property, String anotherProperty) {
		return new FieldExpression(property, anotherProperty, ComparisonOperator.GT);
	}

	public static Expression lteProperty(String property, String anotherProperty) {
		return new FieldExpression(property, anotherProperty, ComparisonOperator.LTE);
	}

	public static Expression gteProperty(String property, String anotherProperty) {
		return new FieldExpression(property, anotherProperty, ComparisonOperator.GTE);
	}

	public static Expression isNull(String property) {
		return new NullableExpression(property, true);
	}

	public static Expression isNotNull(String property) {
		return new NullableExpression(property, false);
	}

	public static Expression text(String property, String text) {
		return new TextExpression(property, text);
	}

	public static Expression exists(From query) {
		return new ExistsExpression(query);
	}

	public static Expression like(String property, String like, MatchMode matchMode) {
		return new LikeExpression(property, like, matchMode);
	}

	public static Expression query(From query) {
		return new QueryExpression(query);
	}

	public static Expression in(String property, Object[] parameters) {
		return new InExpression(property, parameters);
	}

	public static Expression in(String property, From query) {
		return new InExpression(property, query(query));
	}

	public static Expression between(String property, Object lower, Object high) {
		return new BetweenExpression(property, lower, high);
	}

	private Expressions() {
	}

	public static void main(String[] args) {
	}

}
