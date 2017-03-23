package lazycat.series.sqljam.generator;

import com.mysql.fabric.xmlrpc.base.Data;

import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.feature.Feature;

/**
 * CurrentDateGenerator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class CurrentDateGenerator implements Generator {

	public String getText(Feature feature, Session session) {
		return "?";
	}

	public boolean hasValue(Feature feature, Session session) {
		return true;
	}

	public Object getValue(Feature feature, Session session) {
		return new Data();
	}

}
