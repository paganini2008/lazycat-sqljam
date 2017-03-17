package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Member
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Member implements Expression {

	private final Expression expression;

	public Member(String property) {
		this.expression = new Column(property);
	}

	public Member(Expression expression) {
		this.expression = expression;
	}

	public String getText(Translator translator, Configuration configuration) {
		return expression.getText(translator, configuration);
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		expression.setParameter(translator, parameterCollector, configuration);
	}

}
