package lazycat.series.sqljam;

import lazycat.series.sqljam.ORMException;

/**
 * ConnectionException
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ConnectionException extends ORMException {

	private static final long serialVersionUID = -8961949543530857204L;

	public ConnectionException(String msg) {
		super(msg);
	}

	public ConnectionException(Throwable e) {
		super(e);
	}

	public ConnectionException(String msg, Throwable e) {
		super(msg, e);
	}

}
