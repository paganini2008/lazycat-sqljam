package lazycat.series.sqljam;

/**
 * SQL92 Grammar
 * 
 * @author Fred Feng
 * @version 1.0
 */
public abstract class SQL92Criterion {

	public String dropTable(String table) {
		return "drop table if exists " + table;
	}

	public String createTable(String str) {
		return "create table if not exists " + str;
	}

	public String createTableAs(String table, String select) {
		return "create table " + table + " as " + select;
	}

	public String insert(String table) {
		return "insert into " + table;
	}

	public String into(String table) {
		return " into " + table;
	}

	public String wrap(String str) {
		return "(" + str + ")";
	}

	public String using(String str) {
		return "using(" + str + ")";
	}

	public String values(String str) {
		return " values " + str;
	}

	public String update(String str) {
		return " update " + str;
	}

	public String delete() {
		return "delete ";
	}

	public String distinct(String str) {
		return "distinct " + str;
	}

	public String select(boolean distinct) {
		return distinct ? "select distinct " : "select ";
	}

	public String tableAs(String left, String right) {
		return left + " as " + right;
	}

	public String columnAs(String left, String right) {
		return left + " as " + right;
	}

	public String set(String str) {
		return " set " + str;
	}

	public String from(String str) {
		return " from " + str;
	}

	public String where(String str) {
		return " where " + str;
	}

	public String on(String left, String right) {
		return left + " on " + right;
	}

	public String nullable(String str, boolean yes) {
		return str + (yes ? " is null" : " is not null");
	}

	public String nullable(boolean yes) {
		return yes ? " null" : " not null";
	}

	public String and() {
		return " and ";
	}

	public String or() {
		return " or ";
	}

	public String not() {
		return " not ";
	}

	public String exists(String str) {
		return " exists (" + str + ")";
	}

	public String between(String str, String left, String right) {
		return str + " between " + left + " and " + right;
	}

	public String in(String left, String right) {
		return left + " in (" + right + ")";
	}

	public String like(String str, String like, Character escapeChar) {
		return str + " like " + like + (escapeChar != null ? " escape \'" + escapeChar + "\'" : "");
	}

	public String eq(String left, String right) {
		return left + "=" + right;
	}

	public String ne(String left, String right) {
		return left + "!=" + right;
	}

	public String gt(String left, String right) {
		return left + ">" + right;
	}

	public String lt(String left, String right) {
		return left + "<" + right;
	}

	public String gte(String left, String right) {
		return left + ">=" + right;
	}

	public String lte(String left, String right) {
		return left + "<=" + right;
	}

	public String neAny(String left, String right) {
		return left + "!= any " + right;
	}

	public String gtAny(String left, String right) {
		return left + "> any " + right;
	}

	public String ltAny(String left, String right) {
		return left + "< any " + right;
	}

	public String gteAny(String left, String right) {
		return left + ">= any " + right;
	}

	public String lteAny(String left, String right) {
		return left + "<= any " + right;
	}

	public String neAll(String left, String right) {
		return left + "!= all " + right;
	}

	public String gtAll(String left, String right) {
		return left + "> all" + right;
	}

	public String ltAll(String left, String right) {
		return left + "< all " + right;
	}

	public String gteAll(String left, String right) {
		return left + ">= all " + right;
	}

	public String lteAll(String left, String right) {
		return left + "<= all " + right;
	}

	public String plus(String left, String right) {
		return left + "+" + right;
	}

	public String subtract(String left, String right) {
		return left + "-" + right;
	}

	public String divide(String left, String right) {
		return left + "/" + right;
	}

	public String multiply(String left, String right) {
		return left + "*" + right;
	}

	public String modulo(String left, String right) {
		return left + "%" + right;
	}

	public String concat(String left, String right) {
		return left + "||" + right;
	}

	public String bitXor(String left, String right) {
		return left + "^" + right;
	}

	public String bitOr(String left, String right) {
		return left + "|" + right;
	}

	public String bitAnd(String left, String right) {
		return left + "&" + right;
	}

	public String innerJoin(String str) {
		return " inner join " + str;
	}

	public String leftJoin(String str) {
		return " left outter join " + str;
	}

	public String rightJoin(String str) {
		return " right outter join " + str;
	}

	public String fullJoin(String str) {
		return " full join " + str;
	}

	public String crossJoin(String str) {
		return " cross join " + str;
	}

	public String asc(String str) {
		return str + " asc";
	}

	public String desc(String str) {
		return str + " desc";
	}

	public String order(String str) {
		return " order by " + str;
	}

	public String groupBy(String str) {
		return " group by " + str;
	}

	public String having(String str) {
		return " having " + str;
	}

	public String union(String left, String right) {
		return new StringBuilder().append("(").append(left).append(")").append(" union ").append("(").append(right).append(")").toString();
	}

	public String minus(String left, String right) {
		return new StringBuilder().append("(").append(left).append(")").append(" minus ").append("(").append(right).append(")").toString();
	}

	public String intersect(String left, String right) {
		return new StringBuilder().append("(").append(left).append(")").append(" intersect ").append("(").append(right).append(")")
				.toString();
	}

	public String unionAll(String left, String right) {
		return new StringBuilder().append("(").append(left).append(")").append(" union all ").append("(").append(right).append(")")
				.toString();
	}

	public String testSql() {
		return "select 1";
	}

	public String selectCurrval(String seq) {
		return "select " + currval(seq) + " from dual";
	}

	public String selectNextval(String seq) {
		return "select " + nextval(seq) + " from dual";
	}

	public String nextval(String seq) {
		return seq + ".nextval";
	}

	public String currval(String seq) {
		return seq + ".currval";
	}

	public String onDelete(CascadeAction action) {
		switch (action) {
		case NO_ACTION:
			return "on delete no action";
		case CASCADE:
			return "on delete cascade";
		case SET_NULL:
			return "on delete set null";
		case SET_DEFAULT:
			return "on delete set default";
		}
		throw new UnsupportedOperationException();
	}

	public String onUpdate(CascadeAction action) {
		switch (action) {
		case NO_ACTION:
			return "on update no action";
		case CASCADE:
			return "on update cascade";
		case SET_NULL:
			return "on update set null";
		case SET_DEFAULT:
			return "on update set default";
		}
		throw new UnsupportedOperationException();
	}

	public String forUpdate(int timeout) {
		if (timeout < 0) {
			return " for update";
		} else if (timeout > 0) {
			return " for update wait " + timeout;
		} else {
			return " for update nowait";
		}
	}

	public String and(String left, String right) {
		return left + " and " + right;
	}

	public String or(String left, String right) {
		return left + " or " + right;
	}

	public String not(String str) {
		return " not " + str;
	}

	public String max(String str) {
		return "max(" + str + ")";
	}

	public String min(String str) {
		return "min(" + str + ")";
	}

	public String count(String str) {
		return "count(" + str + ")";
	}

	public String avg(String str) {
		return "avg(" + str + ")";
	}

	public String sum(String str) {
		return "sum(" + str + ")";
	}

}
