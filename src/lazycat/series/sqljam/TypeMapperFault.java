package lazycat.series.sqljam;


public class TypeMapperFault extends ORMException {

	private static final long serialVersionUID = 7594444219631343305L;

	public TypeMapperFault(String msg) {
		super(msg);
	}

	public TypeMapperFault(String msg, Throwable cause) {
		super(msg, cause);
	}

	public TypeMapperFault(Throwable cause) {
		super(cause);
	}

}
