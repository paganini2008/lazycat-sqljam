package lazycat.series.sqljam;

/**
 * ConnectionException
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class NullValueException extends ValidationException {

	private static final long serialVersionUID = 7934929643017373898L;

	public NullValueException(String msg) {
		super(msg);
	}

	public NullValueException(String msg, Throwable e) {
		super(msg, e);
	}

}
