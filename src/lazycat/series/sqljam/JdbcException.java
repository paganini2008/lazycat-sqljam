package lazycat.series.sqljam;

/**
 * JdbcException
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class JdbcException extends ORMException {

	private static final long serialVersionUID = 8401594914718233881L;

	public JdbcException(String msg) {
		super(msg);
	}

	public JdbcException(Throwable e) {
		super(e);
	}

	public JdbcException(String msg, Throwable e) {
		super(msg, e);
	}

}
