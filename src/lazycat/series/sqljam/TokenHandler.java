package lazycat.series.sqljam;

import lazycat.series.sqljam.TableMetadata;
import lazycat.series.sqljam.Translator;

/**
 * TokenHandler
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface TokenHandler {

	static final String DEFAULT_TOKEN = "?";

	String getText(Object argument, Translator translator, TableMetadata configuration);

}
