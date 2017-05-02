package lazycat.series.sqljam.expression;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lazycat.series.converter.TypeConverter;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * DateData
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class DateData extends Column implements Data{

	private static final DateFormat df = new SimpleDateFormat("'yyyy-MM-dd HH:mm:ss'");
	private final Object object;

	public DateData(Object object) {
		this.object = object;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		TypeConverter typeConverter = session.getSessionAdmin().getSessionOptions().getTypeConverter();
		Date result = typeConverter.convert(object, Date.class, null);
		return result != null ? df.format(result) : "NULL";
	}

}
