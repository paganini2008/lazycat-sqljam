package lazycat.series.sqljam.field;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * Text
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Text implements Data {

	public static final Text NULL = new Text("NULL");
	private final String text;

	public Text(Object text) {
		this.text = text.toString();
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return text;
	}

}
