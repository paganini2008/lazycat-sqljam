package lazycat.series.sqljam.expression;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * FieldCollector
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ArgumentsCollector {

	private final Map<Integer, Field> fields = new LinkedHashMap<Integer, Field>();
	private final Map<Integer, Object> parameters = new LinkedHashMap<Integer, Object>();
	private final Map<Integer, DataType> sqlTypes = new LinkedHashMap<Integer, DataType>();
	private int size = 0;

	public ArgumentsCollector addParameter(Object parameter) {
		addParameter(parameter, DataType.VARCHAR);
		return this;
	}

	public ArgumentsCollector addParameter(Object parameter, DataType sqlType) {
		parameters.put(size, parameter);
		sqlTypes.put(size, sqlType);
		size++;
		return this;
	}

	public ArgumentsCollector addNull() {
		fields.put(size, Text.NULL);
		size++;
		return this;
	}

	public ArgumentsCollector addColumn(String property) {
		return addColumn(new StandardColumn(property));
	}

	public ArgumentsCollector addColumn(Column column) {
		fields.put(size, column);
		size++;
		return this;
	}

	private Field getField(int index) {
		Field field = fields.get(index);
		if (field == null) {
			Object o = parameters.get(index);
			field = sqlTypes.get(index).createData(o);
		}
		return field;
	}

	public List<Field> fieldList() {
		List<Field> results = new ArrayList<Field>();
		for (int i = 0; i < size; i++) {
			results.add(getField(i));
		}
		return results;
	}

	public int getSize() {
		return size;
	}

	public static ArgumentsCollector create(Object... args) {
		ArgumentsCollector argCollector = new ArgumentsCollector();
		for (Object arg : args) {
			if (arg == null) {
				argCollector.addNull();
			} else if (arg instanceof Column) {
				argCollector.addColumn((Column) arg);
			} else if (arg instanceof Number) {
				argCollector.addParameter(arg, DataType.NUMBER);
			} else if (arg instanceof Date) {
				argCollector.addParameter(arg, DataType.DATE);
			} else if (arg instanceof String) {
				String s = (String) arg;
				if (s.startsWith("'") && s.endsWith("'")) {
					s = s.substring(1, s.length() - 1);
					argCollector.addColumn(s);
				} else {
					argCollector.addParameter(arg, DataType.VARCHAR);
				}
			} else {
				throw new UnsupportedOperationException("Unknown type: " + arg.getClass());
			}
		}
		return argCollector;
	}

}
