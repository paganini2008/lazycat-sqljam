package lazycat.series.sqljam.relational;

import lazycat.series.sqljam.expression.DataType;

/**
 * DefaultEditor
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface DefaultEditor {

	DefaultEditor setValue(String value);

	DefaultEditor setDataType(DataType dataType);

	DefaultDefinition getDefaultDefinition();

}
