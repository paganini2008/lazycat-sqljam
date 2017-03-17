package lazycat.series.sqljam.expression;

import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Property
 * 
 * @author Fred Feng
 * @version 1.0
 */
@Field
public class Property implements Expression {

	private final String tableAlias;
	private final String property;

	public Property(String property) {
		this(null, property);
	}

	public Property(String tableAlias, String property) {
		this.tableAlias = tableAlias;
		this.property = property;
	}

	public String getText(Translator translator, Configuration configuration) {
		String tableAlias = translator.getTableAlias(this.tableAlias);
		String prefix = StringUtils.isNotBlank(tableAlias) ? tableAlias + "." : "";
		String columnName = translator.getColumnName(tableAlias, property, configuration.getMetaData());
		return prefix + columnName;
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
