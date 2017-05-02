package lazycat.series.sqljam.relational;

import java.util.HashMap;
import java.util.Map;

import lazycat.series.sqljam.generator.CurrentTimestampGenerator;
import lazycat.series.sqljam.generator.Generator;
import lazycat.series.sqljam.generator.GuidGenerator;
import lazycat.series.sqljam.generator.NowGenerator;

/**
 * SchemaEditorImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SchemaEditorImpl implements SchemaEditor {

	private final StandardSchemaDefinition schemaDefinition;

	public SchemaEditorImpl(String schema) {
		this.schemaDefinition = new StandardSchemaDefinition(schema);

		registerGenerator(NowGenerator.NAME, new NowGenerator());
		registerGenerator(CurrentTimestampGenerator.CURRENT_TIMESTAMP, new CurrentTimestampGenerator());
		registerGenerator(GuidGenerator.NAME, new GuidGenerator());
	}

	public SchemaDefinition getSchemaDefinition() {
		return schemaDefinition;
	}

	public SequenceEditor addSequence(String sequenceName) {
		SequenceEditor sequenceEditor = new SequenceEditorImpl(this, sequenceName);
		schemaDefinition.sequences.put(sequenceName, sequenceEditor.getSequenceDefinition());
		return sequenceEditor;
	}

	public SchemaEditor registerGenerator(String generatorType, Generator generator) {
		return registerGenerator(generatorType, "global", generator);
	}

	public SchemaEditor registerGenerator(String generatorType, String name, Generator generator) {
		Map<String, Generator> data = schemaDefinition.generators.get(generatorType);
		if (data == null) {
			schemaDefinition.generators.put(generatorType, new HashMap<String, Generator>());
			data = schemaDefinition.generators.get(generatorType);
		}
		data.put(name, generator);
		return this;
	}

}
