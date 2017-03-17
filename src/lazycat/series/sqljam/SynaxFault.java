package lazycat.series.sqljam;

/**
 * SynaxFault
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class SynaxFault extends ORMException {

	public SynaxFault() {
		super();
	}

	public SynaxFault(String msg) {
		super(msg);
	}

	public SynaxFault(String msg, Throwable e) {
		super(msg, e);
	}

	public SynaxFault(Throwable e) {
		super(e);
	}

	private static final long serialVersionUID = 585445004667392687L;

}
