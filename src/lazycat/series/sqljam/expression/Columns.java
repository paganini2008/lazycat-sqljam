package lazycat.series.sqljam.expression;

/**
 * Expressions
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Columns {

	public static Column add(String property, String anotherProperty) {
		return new StandardColumn(property).add(anotherProperty);
	}

	public static Column add(String property, Number number) {
		return new StandardColumn(property).add(number);
	}

	public static Column subtract(String property, String anotherProperty) {
		return new StandardColumn(property).add(anotherProperty);
	}

	public static Column subtract(String property, Number number) {
		return new StandardColumn(property).add(number);
	}

	public static Column multiply(String property, String anotherProperty) {
		return new StandardColumn(property).multiply(anotherProperty);
	}

	public static Column multiply(String property, Number number) {
		return new StandardColumn(property).multiply(number);
	}

	public static Column divide(String property, Number number) {
		return new StandardColumn(property).divide(number);
	}

	public static Column mod(String property, Number number) {
		return new StandardColumn(property).modulo(number);
	}

	public static Column id(String tableAlias) {
		return new ID(tableAlias);
	}

	public static Column forName(String expression) {
		return new StandardColumn(expression);
	}

	public static Column distinct(String property) {
		return new Distinct(forName(property));
	}

	private Columns() {
	}

}
