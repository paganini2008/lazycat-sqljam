package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * TableName
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class TableName implements Expression {

	private final String tableAlias;

	public TableName(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public String getText(Translator translator, Configuration configuration) {
		return translator.getTableName(tableAlias, configuration.getMetaData());
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
