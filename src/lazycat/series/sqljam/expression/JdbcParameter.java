package lazycat.series.sqljam.expression;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * JdbcParameter
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class JdbcParameter implements Expression {

	private final Object parameter;
	private final JdbcType jdbcType;

	public JdbcParameter(Object parameter, JdbcType jdbcType) {
		this.parameter = parameter;
		this.jdbcType = jdbcType;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return "?";
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		parameterCollector.setParameter(parameter, jdbcType);
	}

}
