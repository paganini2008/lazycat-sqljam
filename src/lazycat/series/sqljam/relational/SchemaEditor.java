package lazycat.series.sqljam.relational;

import lazycat.series.sqljam.generator.IdentifierGenerator;

/**
 * SchemaEditor
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface SchemaEditor {

	SchemaDefinition getSchemaDefinition();

	SequenceEditor addSequence(String sequenceName, boolean assigned); 

	void addIdentifierGenerator(String generatorName, IdentifierGenerator identifierGenerator);

}