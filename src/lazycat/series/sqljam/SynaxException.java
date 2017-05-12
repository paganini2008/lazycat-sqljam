package lazycat.series.sqljam;

/**
 * SynaxFault
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SynaxException extends ORMException {

	public SynaxException() {
		super();
	}

	public SynaxException(String msg) {
		super(msg);
	}

	public SynaxException(String msg, Throwable e) {
		super(msg, e);
	}

	public SynaxException(Throwable e) {
		super(e);
	}

	private static final long serialVersionUID = 585445004667392687L;

}
