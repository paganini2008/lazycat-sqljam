package lazycat.series.sqljam.field;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * Distinct
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Distinct extends Column {

	private final Field expression;

	public Distinct(Field expression) {
		this.expression = expression;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		String text = expression.getText(session, translator, configuration);
		return configuration.getJdbcAdmin().getFeature().distinct(text);
	}

}
