package lazycat.series.sqljam;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lazycat.series.jdbc.GeneratedKeyHolder;
import lazycat.series.jdbc.KeyHolder;
import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.PrimaryKeyDefinition;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * GeneratedKeyStore
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class GeneratedKeyStore implements KeyStore {

	private final KeyHolder keyHolder;
	private final Map<String, String> keyMap;

	public GeneratedKeyStore(TableDefinition tableDefinition) {
		this.keyMap = new HashMap<String, String>();
		for (PrimaryKeyDefinition pdDefinition : tableDefinition.getPrimaryKeyDefinitions()) {
			ColumnDefinition cd = pdDefinition.getColumnDefinition();
			this.keyMap.put(cd.getMappedProperty(), cd.getColumnName());
		}
		this.keyHolder = new GeneratedKeyHolder();
		for (String keyName : this.keyMap.values()) {
			this.keyHolder.addKeyName(keyName);
		}
	}

	public Object getKey(String keyProperty) {
		return keyHolder.getKey(keyMap.get(keyProperty));
	}

	public KeyHolder getKeyHolder() {
		return keyHolder;
	}

	public Number getKey() {
		return keyHolder.getKey();
	}

	public Set<String> getKeyNames() {
		return new HashSet<String>(keyMap.keySet());
	}

}
