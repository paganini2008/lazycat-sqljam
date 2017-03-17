package lazycat.series.sqljam.relational;

import lazycat.series.sqljam.generator.IdentifierGenerator;

/**
 * SchemaEditorImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SchemaEditorImpl implements SchemaEditor {

	private final StandardSchemaDefinition schemaDefinition;

	public SchemaEditorImpl(String catalog, String schema) {
		this.schemaDefinition = new StandardSchemaDefinition(catalog, schema);
	}

	public SchemaDefinition getSchemaDefinition() {
		return schemaDefinition;
	}

	public SequenceEditor addSequence(String sequenceName, boolean assigned) {
		SequenceEditor sequenceEditor = new SequenceEditorImpl(this, sequenceName, assigned);
		schemaDefinition.sequences.put(sequenceName, sequenceEditor.getSequenceDefinition());
		return sequenceEditor;
	}

	public void addIdentifierGenerator(String generatorName, IdentifierGenerator identifierGenerator) {
		schemaDefinition.identifiers.put(generatorName, identifierGenerator);
	}

}
