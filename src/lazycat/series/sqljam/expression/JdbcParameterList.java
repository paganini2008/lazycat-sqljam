package lazycat.series.sqljam.expression;

import java.util.ArrayList;
import java.util.List;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * JdbcParameterList
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class JdbcParameterList implements Expression {

	private final List<JdbcParameter> parameterList = new ArrayList<JdbcParameter>();

	public JdbcParameterList() {
	}

	private String delimeter = ",";

	public void setDelimeter(String delimeter) {
		this.delimeter = delimeter;
	}

	public JdbcParameterList addParameter(Object parameter, JdbcType jdbcType) {
		parameterList.add(new JdbcParameter(parameter, jdbcType));
		return this;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		StringBuilder text = new StringBuilder();
		int i = 0;
		for (JdbcParameter parameter : parameterList) {
			text.append(parameter.getText(session, translator, configuration));
			if (i++ != parameterList.size() - 1) {
				text.append(delimeter);
			}
		}
		return text.toString();
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		for (JdbcParameter parameter : parameterList) {
			parameter.setParameter(session, translator, parameterCollector, configuration);
		}
	}

	public static JdbcParameterList create(Object parameter, JdbcType jdbcType) {
		return new JdbcParameterList().addParameter(parameter, jdbcType);
	}

	public static JdbcParameterList create(Object[] parameters, JdbcType jdbcType) {
		JdbcParameterList list = new JdbcParameterList();
		for (Object parameter : parameters) {
			list.addParameter(parameter, jdbcType);
		}
		return list;
	}

}
