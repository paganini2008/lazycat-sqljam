package lazycat.series.sqljam;

/**
 * TypeMapperException
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class TypeMapperException extends ORMException {

	private static final long serialVersionUID = 7594444219631343305L;

	public TypeMapperException(String msg) {
		super(msg);
	}

	public TypeMapperException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public TypeMapperException(Throwable cause) {
		super(cause);
	}

}
