package lazycat.series.sqljam.expression;

/**
 * Expressions
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Fields {

	public static final ID ID = new ID();

	public static Expression plus(String propertyName, Number number) {
		return new NumberExpression(propertyName, number, ArithmeticOperator.PLUS);
	}

	public static Expression subtract(String propertyName, Number number) {
		return new NumberExpression(propertyName, number, ArithmeticOperator.SUBTRACT);
	}

	public static Expression multiply(String propertyName, Number number) {
		return new NumberExpression(propertyName, number, ArithmeticOperator.MULTIPLY);
	}

	public static Expression divide(String propertyName, Number number) {
		return new NumberExpression(propertyName, number, ArithmeticOperator.DIVIDE);
	}

	public static Expression mod(String propertyName, Number number) {
		return new NumberExpression(propertyName, number, ArithmeticOperator.MOD);
	}

	public static Expression id(String tableAlias) {
		return new ID(tableAlias);
	}

	public static Expression label(String name) {
		return new Label(name);
	}

	public static Expression column(String expression) {
		return new Column(expression);
	}

	public static Expression function(String functionName, Object... parameters) {
		return new Function(functionName, parameters);
	}

	public static Expression distinct(String propertyName) {
		return new Distinct(propertyName);
	}

	public static Expression max(String propertyName) {
		return new Function("max", column(propertyName));
	}

	public static Expression min(String propertyName) {
		return new Function("min", column(propertyName));
	}

	public static Expression avg(String propertyName) {
		return new Function("avg", column(propertyName));
	}

	public static Expression sum(String propertyName) {
		return new Function("sum", column(propertyName));
	}

	public static Expression count(String propertyName) {
		return new Function("count", column(propertyName));
	}

	public static Expression countDistinct(String property) {
		return new Function("count", distinct(property));
	}

	private Fields() {
	}

}
