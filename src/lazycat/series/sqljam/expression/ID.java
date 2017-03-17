package lazycat.series.sqljam.expression;

import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.TranslatorException;

/**
 * ID,PrimaryKey
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ID implements Expression {

	private final String tableAlias;
	private final boolean named;

	public ID() {
		this(null);
	}

	public ID(String tableAlias) {
		this(tableAlias, false);
	}

	public ID(String tableAlias, boolean named) {
		this.tableAlias = tableAlias;
		this.named = named;
	}

	public String getText(Translator translator, Configuration configuration) {
		String tableAlias = translator.getTableAlias(this.tableAlias);
		if (named) {
			tableAlias = translator.getTableName(tableAlias, configuration.getMetaData());
		}
		String keyName = translator.getPrimaryKeyName(tableAlias, configuration.getMetaData());
		if (StringUtils.isBlank(keyName)) {
			throw new TranslatorException("No PrimaryKeys.");
		}
		String prefix = StringUtils.isNotBlank(tableAlias) ? tableAlias + "." : "";
		return prefix + keyName;
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}
}
