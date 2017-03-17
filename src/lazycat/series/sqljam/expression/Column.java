package lazycat.series.sqljam.expression;

import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * A column with name or expression
 * 
 * @author Fred Feng
 * @version 1.0
 */
@Field
public class Column implements Expression {

	private static final char TABLE_ALIAS_SEPARATOR = '.';

	private final String expression;
	private final boolean named;

	public Column(String expression) {
		this(expression, false);
	}

	public Column(String expression, boolean named) {
		this.expression = expression;
		this.named = named;
	}

	public String getText(Translator translator, Configuration configuration) {
		String tableAlias, propertyName;
		int index;
		if ((index = expression.indexOf(TABLE_ALIAS_SEPARATOR)) == -1) {
			tableAlias = null;
			propertyName = expression;
		} else {
			tableAlias = expression.substring(0, index);
			propertyName = expression.substring(index + 1);
		}
		tableAlias = translator.getTableAlias(tableAlias);
		if (named) {
			tableAlias = translator.getTableName(tableAlias, configuration.getMetaData());
		}
		String columnName = translator.getColumnName(tableAlias, propertyName, configuration.getMetaData());
		if (StringUtils.isNotBlank(columnName)) {
			return StringUtils.isNotBlank(tableAlias) ? tableAlias + TABLE_ALIAS_SEPARATOR + columnName : columnName;
		} else {
			return StringUtils.isNotBlank(tableAlias) ? tableAlias + TABLE_ALIAS_SEPARATOR + propertyName : propertyName;
		}
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
