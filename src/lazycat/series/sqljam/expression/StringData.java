package lazycat.series.sqljam.expression;

import lazycat.series.converter.TypeConverter;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * StringData
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class StringData extends Column implements Data {

	private final Object object;

	public StringData(Object object) {
		this.object = object;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		TypeConverter typeConverter = session.getSessionAdmin().getSessionOptions().getTypeConverter();
		String result = typeConverter.convert(object, String.class, null);
		return result != null ? "'" + result + "'" : "NULL";
	}

}
