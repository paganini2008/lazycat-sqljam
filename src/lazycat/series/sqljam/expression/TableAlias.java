package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * TableAlias
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class TableAlias implements Expression {

	private final String tableAlias;

	public TableAlias() {
		this("");
	}

	public TableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public String getText(Translator translator, Configuration configuration) {
		return translator.getTableAlias(tableAlias);
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
