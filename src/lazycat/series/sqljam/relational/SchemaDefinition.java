package lazycat.series.sqljam.relational;

import lazycat.series.sqljam.generator.IdentifierGenerator;

/**
 * SchemaDefinition
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface SchemaDefinition {

	String getCatalog();

	String getSchema();

	SequenceDefinition getSequence(String sequenceName);

	IdentifierGenerator getIdentifierGenerator(String generatorName);

}
