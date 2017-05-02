package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * Concat
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Concat extends Column {

	public Concat(String property, String anotherProperty) {
		this(property, new StandardColumn(property));
	}

	public Concat(String property, Field field) {
		this(new StandardColumn(property), field);
	}

	public Concat(Field field, String property) {
		this(field, new StandardColumn(property));
	}

	public Concat(Field left, Field right) {
		this.left = left;
		this.right = right;
	}

	private final Field left;
	private final Field right;

	public String getText(Session session, Translator translator, Configuration configuration) {
		String a = left.getText(session, translator, configuration);
		String b = right.getText(session, translator, configuration);
		return configuration.getJdbcAdmin().getFeature().concat(a, b);
	}

}
