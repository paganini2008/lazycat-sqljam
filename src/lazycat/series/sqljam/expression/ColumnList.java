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
public class ColumnList implements Expression {

	private final List<Column> columns = new ArrayList<Column>();
	private String delimeter = ",";

	public ColumnList() {
	}

	public ColumnList addColumn(String property) {
		return addColumn(property, false);
	}

	public ColumnList addColumn(String property, boolean named) {
		columns.add(new Column(property, named));
		return this;
	}

	public void setDelimeter(String delimeter) {
		this.delimeter = delimeter;
	}

	public String getText(Translator translator, Configuration configuration) {
		StringBuilder text = new StringBuilder();
		int i = 0;
		for (Column column : columns) {
			text.append(column.getText(translator, configuration));
			if (i++ != columns.size() - 1) {
				text.append(delimeter);
			}
		}
		return text.toString();
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		for (Column column : columns) {
			column.setParameter(translator, parameterCollector, configuration);
		}
	}

	public static ColumnList create(String property) {
		return create(property, false);
	}

	public static ColumnList create(String property, boolean named) {
		return new ColumnList().addColumn(property, named);
	}

	public static ColumnList create(String[] properties) {
		return create(properties, false);
	}

	public static ColumnList create(String[] properties, boolean named) {
		ColumnList list = new ColumnList();
		for (String property : properties) {
			list.addColumn(property, named);
		}
		return list;
	}

}
