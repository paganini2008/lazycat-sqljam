package lazycat.series.sqljam.transcation;

import lazycat.series.sqljam.ORMException;

/**
 * TransactionFault
 * @author Fred Feng
 * @version 1.0
 */
public class TransactionException extends ORMException {

	public TransactionException(String msg, Throwable e) {
		super(msg, e);
	}

	public TransactionException(String msg) {
		super(msg);
	}

	public TransactionException(Throwable e) {
		super(e);
	}

	private static final long serialVersionUID = 6385468128497313805L;

}
