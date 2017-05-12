package lazycat.series.sqljam.field;

import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.TranslatorException;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * ID,PrimaryKey
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ID extends Column {

	public static final ID THIS = new ID();
	private final Table table;

	ID() {
		this(null);
	}

	public ID(String expression) {
		this.table = Table.get(expression);
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		String tableAlias = table.getText(session, translator, configuration);
		TableDefinition tableDefinition = translator.getTableDefinition(tableAlias, configuration);
		if (!tableDefinition.hasPrimaryKey()) {
			throw new TranslatorException("No primary keys.");
		}
		String keyName = tableDefinition.getPrimaryKeyDefinition().getColumnDefinition().getColumnName();
		return (StringUtils.isNotBlank(tableAlias) ? tableAlias + "." : "") + keyName;
	}

}
