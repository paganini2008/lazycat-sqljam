package lazycat.series.sqljam;

/**
 * FunctionTemplate
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class FunctionTemplate {

	private final String template;
	private final int argumentSize;

	public FunctionTemplate(String template, int argumentSize) {
		this.template = template;
		this.argumentSize = argumentSize;
	}

	public String getTemplate() {
		return template;
	}

	public int getArgumentSize() {
		return argumentSize;
	}

	public String toString() {
		return "FunctionTemplate [template=" + template + ", argumentSize=" + argumentSize + "]";
	}

}
