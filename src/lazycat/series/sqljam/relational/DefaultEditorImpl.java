package lazycat.series.sqljam.relational;

import lazycat.series.sqljam.expression.DataType;

/**
 * DefaultEditorImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class DefaultEditorImpl implements DefaultEditor {

	private final StandardDefaultDefinition defaultDefinition;

	public DefaultEditorImpl(TableEditor tableEditor, String propertyName) {
		this.defaultDefinition = new StandardDefaultDefinition(tableEditor.getTableDefinition().getColumnDefinition(propertyName));
	}

	public DefaultEditor setValue(String value) {
		defaultDefinition.setValue(value);
		return this;
	}

	public DefaultEditor setDataType(DataType dataType) {
		defaultDefinition.setDataType(dataType);
		return this;
	}

	public DefaultDefinition getDefaultDefinition() {
		return defaultDefinition;
	}

}
