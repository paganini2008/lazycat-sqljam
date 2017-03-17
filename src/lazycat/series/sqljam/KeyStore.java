package lazycat.series.sqljam;

import java.util.Set;

import lazycat.series.jdbc.KeyHolder;

/**
 * KeyStore
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface KeyStore {

	Number getKey();

	Set<String> getKeyNames();

	Object getKey(String keyProperty);

	KeyHolder getKeyHolder();

}