package lazycat.series.sqljam.expression;

import java.util.ArrayList;
import java.util.List;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.lang.Ints;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.tag.Field;

/**
 * Represent a function
 * 
 * @author Fred Feng
 * @version 1.0
 */
@Field
public class Function implements Expression {

	private final String functionName;
	private final List<Object> parameters = new ArrayList<Object>();

	public Function(String functionName, Object... parameters) {
		this.functionName = functionName;
		for (Object parameter : parameters) {
			this.parameters.add(parameter);
		}
	}

	public String getText(Translator translator, Configuration configuration) {
		String template = configuration.getFeature().getFunctionTemplate(functionName);
		List<Integer> indexes = new ArrayList<Integer>();
		List<String> columns = new ArrayList<String>();
		int i = 0;
		for (Object parameter : parameters) {
			if (parameter instanceof Expression) {
				if (parameter.getClass().isAnnotationPresent(Field.class)) {
					columns.add(((Expression) parameter).getText(translator, configuration));
					indexes.add(i++);
				} else {
					throw new FunctionException("Invalid function parameter. Index: " + i);
				}
			}
		}
		return StringHelper.parseText(template, "?", columns.toArray(), Ints.toIntArray(indexes));
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		for (Object parameter : parameters) {
			if (!(parameter instanceof Expression)) {
				if (parameter != null) {
					parameterCollector.setParameter(parameter, JdbcType.OTHER);
				} else {
					throw new FunctionException(
							"Unable to sure the 'NULL' parameter and its data type. Please reset your function template which can contains it.");
				}
			}
		}
	}

}
