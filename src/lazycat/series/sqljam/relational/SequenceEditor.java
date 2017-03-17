package lazycat.series.sqljam.relational;

/**
 * SequenceEditor
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface SequenceEditor {

	SequenceEditor setStartWith(int startWith);

	SequenceEditor setIncrementBy(int incrementBy);

	SequenceEditor setMaxValue(int maxValue);

	SequenceEditor setMinValue(int minValue);

	SequenceEditor setCache(int cache);

	SequenceDefinition getSequenceDefinition();

}