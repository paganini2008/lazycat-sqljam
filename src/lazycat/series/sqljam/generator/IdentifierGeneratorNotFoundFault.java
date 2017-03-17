package lazycat.series.sqljam.generator;

import lazycat.series.sqljam.ORMException;

/**
 * IdentifierGeneratorNotFoundFault
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class IdentifierGeneratorNotFoundFault extends ORMException {

	private static final long serialVersionUID = -4312229114724742433L;
	
	public IdentifierGeneratorNotFoundFault() {
		super();
	}

	public IdentifierGeneratorNotFoundFault(String msg) {
		super(msg);
	}

	public IdentifierGeneratorNotFoundFault(String msg, Throwable e) {
		super(msg, e);
	}

}
