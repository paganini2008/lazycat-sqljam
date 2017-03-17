package lazycat.series.sqljam.expression;

import java.util.ArrayList;
import java.util.List;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Sql ParameterList
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ParameterList implements Expression {

	private final List<Parameter> parameterList = new ArrayList<Parameter>();

	public ParameterList() {
	}

	private String delimeter = ",";

	public void setDelimeter(String delimeter) {
		this.delimeter = delimeter;
	}

	public ParameterList addParameter(Object parameter, JdbcType jdbcType) {
		parameterList.add(new Parameter(parameter, jdbcType));
		return this;
	}

	public String getText(Translator translator, Configuration configuration) {
		StringBuilder text = new StringBuilder();
		int i = 0;
		for (Parameter parameter : parameterList) {
			text.append(parameter.getText(translator, configuration));
			if (i++ != parameterList.size() - 1) {
				text.append(delimeter);
			}
		}
		return text.toString();
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		for (Parameter parameter : parameterList) {
			parameter.setParameter(translator, parameterCollector, configuration);
		}
	}

	public static ParameterList create(Object parameter, JdbcType jdbcType) {
		return new ParameterList().addParameter(parameter, jdbcType);
	}

	public static ParameterList create(Object[] parameters, JdbcType jdbcType) {
		ParameterList list = new ParameterList();
		for (Object parameter : parameters) {
			list.addParameter(parameter, jdbcType);
		}
		return list;
	}

}
