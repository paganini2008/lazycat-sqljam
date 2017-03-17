package lazycat.series.sqljam;

public class MappingFault extends ORMException {

	private static final long serialVersionUID = 2901130858385490690L;

	public MappingFault(String msg) {
		super(msg);
	}

	public MappingFault(Throwable e) {
		super(e);
	}

	public MappingFault(String msg, Throwable e) {
		super(msg, e);
	}

}
