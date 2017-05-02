package lazycat.series.sqljam.expression;

import java.math.BigDecimal;

import lazycat.series.converter.TypeConverter;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * NumberData
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class NumberData extends ArithmeticalColumn implements Data {

	public static final NumberData ONE = new NumberData(1);
	private final Object value;

	public NumberData(Object value) {
		this.value = value;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		TypeConverter typeConverter = session.getSessionAdmin().getSessionOptions().getTypeConverter();
		BigDecimal result = typeConverter.convert(value, BigDecimal.class, null);
		return result != null ? result.toPlainString() : "NULL";
	}

}
