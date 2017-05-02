package lazycat.series.sqljam.relational;

import java.util.HashMap;
import java.util.Map;

import lazycat.series.sqljam.generator.Generator;

/**
 * StandardSchemaDefinition
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class StandardSchemaDefinition implements SchemaDefinition {

	private final String schema;
	final Map<String, SequenceDefinition> sequences = new HashMap<String, SequenceDefinition>();
	final Map<String, Map<String, Generator>> generators = new HashMap<String, Map<String, Generator>>();

	StandardSchemaDefinition(String schema) {
		this.schema = schema;
	}

	public String getSchema() {
		return schema;
	}

	public SequenceDefinition getSequenceDefinition(String sequenceName) {
		return sequences.get(sequenceName);
	}

	public Generator getGenerator(String generator, String name) {
		Map<String, Generator> data = generators.get(generator);
		return data.get(name);
	}

}
