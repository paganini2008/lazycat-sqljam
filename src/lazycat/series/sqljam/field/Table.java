package lazycat.series.sqljam.field;

import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * Table
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Table extends AbstractField {

	public static final Table THIS = new Table();
	private final String alias;

	Table() {
		this(null);
	}

	public Table(String alias) {
		this.alias = alias;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return StringUtils.isBlank(alias) ? translator.getTableAlias() : alias;
	}

	public static Table get(String alias) {
		return StringUtils.isBlank(alias) ? THIS : new Table(alias);
	}

}
