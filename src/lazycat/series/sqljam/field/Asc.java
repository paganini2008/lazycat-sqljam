package lazycat.series.sqljam.field;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * Asc
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Asc extends AbstractField {

	private final Field field;

	public Asc(String property) {
		this.field = new StandardColumn(property);
	}

	public Asc(Field field) {
		this.field = field;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return configuration.getJdbcAdmin().getFeature().asc(field.getText(session, translator, configuration));
	}

}
