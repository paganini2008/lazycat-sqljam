package lazycat.series.sqljam;

/**
 * ValidationFault
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ValidationException extends ORMException {

	private static final long serialVersionUID = -2187012359620957515L;

	public ValidationException(String msg) {
		super(msg);
	}

	public ValidationException(String msg, Throwable e) {
		super(msg, e);
	}

}
