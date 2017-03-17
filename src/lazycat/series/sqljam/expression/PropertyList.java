package lazycat.series.sqljam.expression;

import java.util.ArrayList;
import java.util.List;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * ColumnList
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class PropertyList implements Expression {

	private final List<Property> properties = new ArrayList<Property>();
	private String delimeter = ",";

	public PropertyList() {
	}

	public void setDelimeter(String delimeter) {
		this.delimeter = delimeter;
	}

	public PropertyList addProperty(String tableAlias, String property) {
		properties.add(new Property(tableAlias, property));
		return this;
	}

	public String getText(Translator translator, Configuration configuration) {
		StringBuilder text = new StringBuilder();
		int i = 0;
		for (Property property : properties) {
			text.append(property.getText(translator, configuration));
			if (i++ != properties.size() - 1) {
				text.append(delimeter);
			}
		}
		return text.toString();
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		for (Property property : properties) {
			property.setParameter(translator, parameterCollector, configuration);
		}
	}

	public static PropertyList create(String tableAlias, String property) {
		return new PropertyList().addProperty(tableAlias, property);
	}

	public static PropertyList create(String tableAlias, String[] properties) {
		PropertyList list = new PropertyList();
		for (String property : properties) {
			list.addProperty(tableAlias, property);
		}
		return list;
	}

}
