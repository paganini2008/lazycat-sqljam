package lazycat.series.sqljam.relational;

import java.util.HashMap;
import java.util.Map;

import lazycat.series.sqljam.generator.IdentifierGenerator;

/**
 * StandardSchemaDefinition
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class StandardSchemaDefinition implements SchemaDefinition {

	private final String catalog;
	private final String schema;
	final Map<String, SequenceDefinition> sequences = new HashMap<String, SequenceDefinition>();
	final Map<String, IdentifierGenerator> identifiers = new HashMap<String, IdentifierGenerator>();

	StandardSchemaDefinition(String catalog, String schema) {
		this.catalog = catalog;
		this.schema = schema;
	}

	public String getCatalog() {
		return catalog;
	}

	public String getSchema() {
		return schema;
	}

	public SequenceDefinition getSequence(String sequenceName) {
		return sequences.get(sequenceName);
	}

	public IdentifierGenerator getIdentifierGenerator(String generatorName) {
		return identifiers.get(generatorName);
	}

}
