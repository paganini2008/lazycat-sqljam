package lazycat.series.sqljam.query;

import lazycat.series.sqljam.SessionException;

public class QueryException extends SessionException {

	private static final long serialVersionUID = -1390548402149623693L;

	public QueryException(String msg) {
		super(msg);
	}

	public QueryException(String msg, Throwable e) {
		super(msg, e);
	}

}
