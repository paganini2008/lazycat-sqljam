package lazycat.series.sqljam.generator;

import lazycat.series.sqljam.ORMException;

/**
 * GeneratorNotFoundException
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class GeneratorNotFoundException extends ORMException {

	private static final long serialVersionUID = -4312229114724742433L;

	public GeneratorNotFoundException() {
		super();
	}

	public GeneratorNotFoundException(String msg) {
		super(msg);
	}

	public GeneratorNotFoundException(String msg, Throwable e) {
		super(msg, e);
	}

}
