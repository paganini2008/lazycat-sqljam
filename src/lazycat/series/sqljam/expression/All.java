package lazycat.series.sqljam.expression;

import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * All '*'
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class All extends AbstractField {

	public static final All THIS = new All();
	private final Table table;

	All() {
		this(null);
	}

	public All(String expression) {
		this.table = new Table(expression);
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		String tableName = table.getText(session, translator, configuration);
		return StringUtils.isNotBlank(tableName) ? tableName + ".*" : "*";
	}

}
