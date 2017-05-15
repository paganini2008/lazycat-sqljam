package lazycat.series.sqljam.field;

public class Functions {

	public static Column max(String property) {
		return max(property, false);
	}

	public static Column min(String property) {
		return min(property, false);
	}

	public static Column avg(String property) {
		return avg(property, false);
	}

	public static Column sum(String property) {
		return sum(property, false);
	}

	public static Column count(String property) {
		return count(property, false);
	}

	public static Column rows() {
		return new NestedFunction("count", NumberData.ONE);
	}

	public static Column rows(String tableAlias) {
		return new NestedFunction("count", new All(tableAlias));
	}

	public static Column max(String property, boolean distinct) {
		return max(new StandardColumn(property), distinct);
	}

	public static Column min(String property, boolean distinct) {
		return min(new StandardColumn(property), distinct);
	}

	public static Column avg(String property, boolean distinct) {
		return avg(new StandardColumn(property), distinct);
	}

	public static Column sum(String property, boolean distinct) {
		return sum(new StandardColumn(property), distinct);
	}

	public static Column count(String property, boolean distinct) {
		return count(new StandardColumn(property), distinct);
	}

	public static Column max(Field field, boolean distinct) {
		return new NestedFunction("max", distinct ? new Distinct(field) : field);
	}

	public static Column min(Field field, boolean distinct) {
		return new NestedFunction("min", distinct ? new Distinct(field) : field);
	}

	public static Column avg(Field field, boolean distinct) {
		return new NestedFunction("avg", distinct ? new Distinct(field) : field);
	}

	public static Column sum(Field field, boolean distinct) {
		return new NestedFunction("sum", distinct ? new Distinct(field) : field);
	}

	public static Column count(Field field, boolean distinct) {
		return new NestedFunction("count", distinct ? new Distinct(field) : field);
	}

	public static Column func(String name, Object... parameters) {
		return new Function(name, parameters);
	}

	private Functions() {
	}

}
