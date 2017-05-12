package lazycat.series.sqljam.field;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * Field
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Field {

	String getText(Session session, Translator translator, Configuration configuration);

}
