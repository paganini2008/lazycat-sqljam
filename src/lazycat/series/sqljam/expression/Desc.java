package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * Desc
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Desc extends AbstractField {

	private final Field field;

	public Desc(String property) {
		this.field = new StandardColumn(property);
	}

	public Desc(Field field) {
		this.field = field;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return configuration.getJdbcAdmin().getFeature().desc(field.getText(session, translator, configuration));
	}

}
