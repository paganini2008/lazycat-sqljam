package lazycat.series.sqljam.expression;

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

	public static Column countOne() {
		return new NamingFunction("count", NumberData.ONE);
	}

	public static Column countAll(String tableAlias) {
		return new NamingFunction("count", new All(tableAlias));
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
		return new NamingFunction("max", distinct ? new Distinct(field) : field);
	}

	public static Column min(Field field, boolean distinct) {
		return new NamingFunction("min", distinct ? new Distinct(field) : field);
	}

	public static Column avg(Field field, boolean distinct) {
		return new NamingFunction("avg", distinct ? new Distinct(field) : field);
	}

	public static Column sum(Field field, boolean distinct) {
		return new NamingFunction("sum", distinct ? new Distinct(field) : field);
	}

	public static Column count(Field field, boolean distinct) {
		return new NamingFunction("count", distinct ? new Distinct(field) : field);
	}

	public static Column func(String name, Object... parameters) {
		return new Function(name, parameters);
	}

	private Functions() {
	}

}
