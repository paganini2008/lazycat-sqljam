package lazycat.series.sqljam;

/**
 * ORMException
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ORMException extends RuntimeException {

	private static final long serialVersionUID = 5084596308246824100L;

	public ORMException() {
		super();
	}

	public ORMException(String msg) {
		super(msg);
	}

	public ORMException(Throwable e) {
		super(e);
	}

	public ORMException(String msg, Throwable e) {
		super(msg, e);
	}

}
