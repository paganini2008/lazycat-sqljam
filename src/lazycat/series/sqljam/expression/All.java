package lazycat.series.sqljam.expression;

import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * All '*'
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class All implements Expression {

	private final String tableAlias;

	public All() {
		this("");
	}

	public All(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public String getText(Translator translator, Configuration configuration) {
		final String tableAlias = translator.getTableAlias(this.tableAlias);
		return StringUtils.isNotBlank(tableAlias) ? tableAlias + ".*" : "*";
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
