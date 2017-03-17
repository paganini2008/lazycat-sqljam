package lazycat.series.sqljam.relational;

/**
 * SequenceEditorImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SequenceEditorImpl implements SequenceEditor {

	private final StandardSequenceDefinition sequenceDefinition;

	SequenceEditorImpl(SchemaEditor schemaEditor, String name, boolean assigned) {
		this.sequenceDefinition = new StandardSequenceDefinition(schemaEditor.getSchemaDefinition(), name, assigned);
	}

	public SequenceEditor setStartWith(int startWith) {
		this.sequenceDefinition.setStartWith(startWith);
		return this;
	}

	public SequenceEditor setIncrementBy(int incrementBy) {
		this.sequenceDefinition.setIncrementBy(incrementBy);
		return this;
	}

	public SequenceEditor setMaxValue(int maxValue) {
		this.sequenceDefinition.setMaxValue(maxValue);
		return this;
	}

	public SequenceEditor setMinValue(int minValue) {
		this.sequenceDefinition.setMinValue(minValue);
		return this;
	}

	public SequenceEditor setCache(int cache) {
		this.sequenceDefinition.setCache(cache);
		return this;
	}

	public SequenceDefinition getSequenceDefinition() {
		return sequenceDefinition;
	}

}
