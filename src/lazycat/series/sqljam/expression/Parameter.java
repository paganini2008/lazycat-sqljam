package lazycat.series.sqljam.expression;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Sql Parameter
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Parameter implements Expression {

	private final Object parameter;
	private final JdbcType jdbcType;

	public Parameter(Object parameter, JdbcType jdbcType) {
		this.parameter = parameter;
		this.jdbcType = jdbcType;
	}

	public String getText(Translator translator, Configuration configuration) {
		return "?";
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		parameterCollector.setParameter(parameter, jdbcType);
	}

}
