package lazycat.series.sqljam.expression;

/**
 * AbstractField
 * 
 * @author Fred Feng
 * @version 1.0
 */
public abstract class AbstractField implements Field {

	public MultiField sibling(Field field) {
		return new MultiField(this, field);
	}

}
