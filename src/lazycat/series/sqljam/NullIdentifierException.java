package lazycat.series.sqljam;

/**
 * IdentifierNullFault
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class NullIdentifierException extends ValidationException {

	private static final long serialVersionUID = -8602615533584189143L;

	public NullIdentifierException() {
		super("");
	}

	public NullIdentifierException(String msg) {
		super(msg);
	}

}
