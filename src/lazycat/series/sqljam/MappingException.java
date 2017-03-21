package lazycat.series.sqljam;

/**
 * MappingException
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class MappingException extends ORMException {

	private static final long serialVersionUID = 2901130858385490690L;

	public MappingException(String msg) {
		super(msg);
	}

	public MappingException(Throwable e) {
		super(e);
	}

	public MappingException(String msg, Throwable e) {
		super(msg, e);
	}

}
