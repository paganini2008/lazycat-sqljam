package lazycat.series.sqljam;

/**
 * TranslatorException
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class TranslatorException extends ORMException {

	private static final long serialVersionUID = -3829575566426425448L;

	public TranslatorException(String msg) {
		super(msg);
	}

	public TranslatorException(String msg, Throwable e) {
		super(msg, e);
	}

}
