package lazycat.series.sqljam.field;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * MultiField
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class MultiField implements Field {

	private final Field left;
	private final Field right;

	public MultiField(Field left, Field right) {
		this.left = left;
		this.right = right;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return left.getText(session, translator, configuration) + ", "
				+ right.getText(session, translator, configuration);
	}

}
