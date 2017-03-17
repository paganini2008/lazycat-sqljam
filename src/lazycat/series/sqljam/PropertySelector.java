package lazycat.series.sqljam;

import lazycat.series.jdbc.JdbcType;

/**
 * PropertySelector
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface PropertySelector {

	boolean include(Object argument, String propertyName, JdbcType type);

}
