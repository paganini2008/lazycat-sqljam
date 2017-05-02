package lazycat.series.sqljam.expression;

import java.util.ArrayList;
import java.util.List;

import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * Template
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Template extends ArithmeticalColumn {

	private final String pattern;
	private final ArgumentsCollector argumentsCollector;

	public Template(String pattern, Object... parameters) {
		this.pattern = pattern;
		this.argumentsCollector = ArgumentsCollector.create(parameters);
	}

	public Template addParameter(Object parameter) {
		argumentsCollector.addParameter(parameter, DataType.VARCHAR);
		return this;
	}

	public Template addParameter(Object parameter, DataType sqlType) {
		argumentsCollector.addParameter(parameter, sqlType);
		return this;
	}

	public Template addColumn(String property) {
		return addColumn(new StandardColumn(property));
	}

	public Template addColumn(Column column) {
		argumentsCollector.addColumn(column);
		return this;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		List<String> args = new ArrayList<String>();
		for (Field field : argumentsCollector.fieldList()) {
			args.add(field.getText(session, translator, configuration));
		}
		return StringUtils.parseText(pattern, "?", args.toArray());
	}

}
