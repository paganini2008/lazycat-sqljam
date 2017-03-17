package lazycat.series.sqljam.transcation;

import lazycat.series.sqljam.ORMException;

/**
 * TransactionFault
 * @author Fred Feng
 * @version 1.0
 */
public class TransactionFault extends ORMException {

	public TransactionFault(String msg, Throwable e) {
		super(msg, e);
	}

	public TransactionFault(String msg) {
		super(msg);
	}

	public TransactionFault(Throwable e) {
		super(e);
	}

	private static final long serialVersionUID = 6385468128497313805L;

}
