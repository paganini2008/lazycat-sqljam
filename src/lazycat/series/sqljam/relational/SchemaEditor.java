package lazycat.series.sqljam.relational;

import lazycat.series.sqljam.generator.Generator;

/**
 * SchemaEditor
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface SchemaEditor {

	SchemaDefinition getSchemaDefinition();

	SequenceEditor addSequence(String sequenceName);

	SchemaEditor registerGenerator(String generatorType, String name, Generator generator);

}