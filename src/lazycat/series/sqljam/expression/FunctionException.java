package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.ORMException;

/**
 * FunctionException
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class FunctionException extends ORMException {

	private static final long serialVersionUID = -811294141199364954L;

	public FunctionException(String msg) {
		super(msg);
	}

	public FunctionException(String msg, Throwable e) {
		super(msg, e);
	}
}
