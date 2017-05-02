package lazycat.series.sqljam.relational;

import lazycat.series.sqljam.generator.Generator;

/**
 * SchemaDefinition
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface SchemaDefinition {

	String getSchema();

	SequenceDefinition getSequenceDefinition(String sequenceName);

	Generator getGenerator(String generator, String name);

}
