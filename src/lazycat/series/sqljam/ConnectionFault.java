package lazycat.series.sqljam;

import lazycat.series.sqljam.ORMException;

/**
 * ConnectionFault
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ConnectionFault extends ORMException {

	private static final long serialVersionUID = -8961949543530857204L;

	public ConnectionFault(String msg) {
		super(msg);
	}

	public ConnectionFault(Throwable e) {
		super(e);
	}

	public ConnectionFault(String msg, Throwable e) {
		super(msg, e);
	}

}
