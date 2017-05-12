package lazycat.series.sqljam.field;

import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * A column with name or expression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class StandardColumn extends MathColumn {

	private final Table table;
	private final String property;

	public StandardColumn(String expression) {
		int index;
		if ((index = expression.indexOf(separator)) == -1) {
			this.table = Table.THIS;
			this.property = expression;
		} else {
			this.table = new Table(expression.substring(0, index));
			this.property = expression.substring(index + 1);
		}
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		String tableAlias = table.getText(session, translator, configuration);
		TableDefinition tableDefinition = translator.getTableDefinition(tableAlias, configuration);
		ColumnDefinition columnDefinition = tableDefinition.getColumnDefinition(property);
		final String name = columnDefinition != null ? columnDefinition.getColumnName() : property;
		return StringUtils.isNotBlank(tableAlias) ? tableAlias + separator + name : name;
	}

}
