package lazycat.series.sqljam;

/**
 * IdentifierNullFault
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class IdentifierNullFault extends ValidationException {

	private static final long serialVersionUID = -2698717892712140700L;

	public IdentifierNullFault() {
		super("");
	}

	public IdentifierNullFault(String msg) {
		super(msg);
	}

}
