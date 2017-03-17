package lazycat.series.sqljam;

/**
 * JdbcFault
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class JdbcFault extends ORMException {

	private static final long serialVersionUID = 8401594914718233881L;

	public JdbcFault(String msg) {
		super(msg);
	}

	public JdbcFault(Throwable e) {
		super(e);
	}

	public JdbcFault(String msg, Throwable e) {
		super(msg, e);
	}

}
