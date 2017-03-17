package lazycat.series.sqljam.relational;

import lazycat.series.sqljam.generator.IdentifierGenerator;

/**
 * SequenceDefinition
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface SequenceDefinition {

	String getName();

	int getStartWith();

	int getIncrementBy();

	int getMaxValue();

	int getMinValue();

	int getCache();

	SchemaDefinition getSchemaDefinition();
	
	IdentifierGenerator getIdentifierGenerator();

}