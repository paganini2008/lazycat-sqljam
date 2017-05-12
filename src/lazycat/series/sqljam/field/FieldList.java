package lazycat.series.sqljam.field;

import java.util.ArrayList;
import java.util.List;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * FieldList
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class FieldList implements Field {

	public FieldList() {
	}

	private final List<Field> fields = new ArrayList<Field>();

	public FieldList addField(Field field) {
		fields.add(field);
		return this;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		StringBuilder text = new StringBuilder();
		int i = 0;
		for (Field field : fields) {
			text.append(field.getText(session, translator, configuration));
			if (i++ != fields.size() - 1) {
				text.append(", ");
			}
		}
		return text.toString();
	}

	public static FieldList list(Field... fields) {
		FieldList list = new FieldList();
		for (Field field : fields) {
			list.addField(field);
		}
		return list;
	}

	public static FieldList columnList(String... properties) {
		FieldList list = new FieldList();
		for (String property : properties) {
			list.addField(new StandardColumn(property).as(property));
		}
		return list;
	}

}
