package lazycat.series.sqljam;

/**
 * DdlResolverFault
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class DdlResolverException extends ORMException {

	private static final long serialVersionUID = 2504501606088031434L;

	public DdlResolverException(String msg) {
		super(msg);
	}

	public DdlResolverException(String msg, Throwable e) {
		super(msg, e);
	}

	public DdlResolverException(Throwable e) {
		super(e);
	}

}
