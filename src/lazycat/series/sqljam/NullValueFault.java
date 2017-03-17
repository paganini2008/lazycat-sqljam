package lazycat.series.sqljam;

/**
 * NonNullFault
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class NullValueFault extends ValidationException {

	private static final long serialVersionUID = 7934929643017373898L;

	public NullValueFault(String msg) {
		super(msg);
	}

	public NullValueFault(String msg, Throwable e) {
		super(msg, e);
	}

}
