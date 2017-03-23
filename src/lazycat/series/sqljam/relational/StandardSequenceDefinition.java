package lazycat.series.sqljam.relational;

import lazycat.series.beans.ToStringBuilder;
import lazycat.series.sqljam.generator.Generator;
import lazycat.series.sqljam.generator.SequenceAssignedGenerator;
import lazycat.series.sqljam.generator.SequenceGenerator;

/**
 * StandardSequenceDefinition
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class StandardSequenceDefinition implements SequenceDefinition {

	private final SchemaDefinition schemaDefinition;
	private final String name;
	private int startWith;
	private int incrementBy;
	private int maxValue;
	private int minValue;
	private int cache;
	private final Generator identifierGenerator;

	StandardSequenceDefinition(SchemaDefinition schemaDefinition, String name, boolean assigned) {
		this.schemaDefinition = schemaDefinition;
		this.name = name;
		this.startWith = 1;
		this.incrementBy = 1;
		this.maxValue = -1;
		this.minValue = 1;
		this.cache = 10;
		this.identifierGenerator = assigned ? new SequenceAssignedGenerator(name)
				: new SequenceGenerator(name);
	}

	public String getName() {
		return name;
	}

	public int getStartWith() {
		return startWith;
	}

	public void setStartWith(int startWith) {
		this.startWith = startWith;
	}

	public int getIncrementBy() {
		return incrementBy;
	}

	public void setIncrementBy(int incrementBy) {
		this.incrementBy = incrementBy;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getCache() {
		return cache;
	}

	public void setCache(int cache) {
		this.cache = cache;
	}

	public SchemaDefinition getSchemaDefinition() {
		return schemaDefinition;
	}

	public Generator getIdentifierGenerator() {
		return identifierGenerator;
	}

	public String toString() {
		return ToStringBuilder.reflectInvokeToString(this, new String[] { "tableDefinition" });
	}

}
