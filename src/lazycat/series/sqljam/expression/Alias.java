package lazycat.series.sqljam.expression;

import lazycat.series.lang.Assert;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * Alias
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Alias extends AbstractField {

	private final Column column;
	private final String alias;

	public Alias(Column column, String alias) {
		Assert.hasNoText(alias, "Undefined alias.");
		this.column = column;
		this.alias = alias;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		String str = column.getText(session, translator, configuration);
		return configuration.getJdbcAdmin().getFeature().columnAs(str, alias);
	}

}
