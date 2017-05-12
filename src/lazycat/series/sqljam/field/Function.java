package lazycat.series.sqljam.field;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * Represent a function
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Function extends MathColumn {

	private final String name;
	private final ArgumentsCollector argumentsCollector;

	public Function(String name, Object... parameters) {
		this.name = name;
		this.argumentsCollector = ArgumentsCollector.create(parameters);
	}

	public Function addParameter(Object parameter) {
		argumentsCollector.addParameter(parameter, DataType.VARCHAR);
		return this;
	}

	public Function addParameter(Object parameter, DataType sqlType) {
		argumentsCollector.addParameter(parameter, sqlType);
		return this;
	}

	public Function addColumn(String property) {
		return addColumn(new StandardColumn(property));
	}

	public Function addColumn(Column column) {
		argumentsCollector.addColumn(column);
		return this;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		StringBuilder sql = new StringBuilder();
		sql.append(name);
		int i = 0;
		for (Field field : argumentsCollector.fieldList()) {
			sql.append(field.getText(session, translator, configuration));
			if (i++ != argumentsCollector.getSize() - 1) {
				sql.append(", ");
			}
		}
		return sql.toString();
	}

}
