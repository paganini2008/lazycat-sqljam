package lazycat.series.sqljam.field;

import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * TableName
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class TableName extends AbstractField {

	public static final TableName THIS = new TableName();
	private final String exp;

	TableName() {
		this(null);
	}

	public TableName(String exp) {
		this.exp = exp;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		String alias = StringUtils.isBlank(exp) ? translator.getTableAlias() : exp;
		return translator.getTableDefinition(alias, configuration).getTableName();
	}

}
