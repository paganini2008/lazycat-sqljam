package lazycat.series.sqljam;

/**
 * SessionFault
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SessionException extends ORMException {

	private static final long serialVersionUID = 7847567616567467109L;

	public SessionException(String msg) {
		super(msg);
	}

	public SessionException(Throwable e) {
		super(e);
	}

	public SessionException(String msg, Throwable e) {
		super(msg, e);
	}

}
