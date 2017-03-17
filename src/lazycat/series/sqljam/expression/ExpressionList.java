package lazycat.series.sqljam.expression;

import java.util.ArrayList;
import java.util.List;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * ExpressionList
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ExpressionList implements Expression {

	private final List<Expression> list = new ArrayList<Expression>();
	private String delimeter = ",";

	public ExpressionList() {
	}

	public void setDelimeter(String delimeter) {
		this.delimeter = delimeter;
	}

	public ExpressionList addExpression(Expression expression) {
		list.add(expression);
		return this;
	}

	public String getText(Translator translator, Configuration configuration) {
		StringBuilder sql = new StringBuilder();
		for (int i = 0, l = list.size(); i < l; i++) {
			sql.append(list.get(i).getText(translator, configuration));
			if (i != l - 1) {
				sql.append(delimeter);
			}
		}
		return sql.toString();
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		for (Expression expression : list) {
			expression.setParameter(translator, parameterCollector, configuration);
		}
	}

	public static ExpressionList create(Expression expression) {
		return new ExpressionList().addExpression(expression);
	}

	public static ExpressionList create(Expression... expressions) {
		ExpressionList list = new ExpressionList();
		for (Expression expression : expressions) {
			list.addExpression(expression);
		}
		return list;
	}

}
