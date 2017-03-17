package lazycat.series.sqljam;

/**
 * Top interface of a sql execution
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Executable {

	/**
	 * Get sql text
	 * 
	 * @param configuration
	 * @return
	 */
	String getText(Configuration configuration);

	/**
	 * Set sql parameters
	 * 
	 * @param parameterCollector
	 * @param configuration
	 */
	void setParameters(ParameterCollector parameterCollector, Configuration configuration);

}
