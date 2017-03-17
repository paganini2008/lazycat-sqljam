package lazycat.series.sqljam.query;

/**
 * A part of the implementation of from clause
 * 
 * @author Fred Feng
 * @version 1.0
 */
public abstract class StandardFrom implements From {

	public From join(Class<?> mappedClass, String tableAlias) {
		return new SimpleFrom(mappedClass, tableAlias, this);
	}

	public From join(From query, String tableAlias) {
		return new View(query, tableAlias, this);
	}
}
