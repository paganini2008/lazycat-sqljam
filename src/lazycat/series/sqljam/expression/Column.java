package lazycat.series.sqljam.expression;

import lazycat.series.lang.StringUtils;

/**
 * Column
 * 
 * @author Fred Feng
 * @version 1.0
 */
public abstract class Column extends AbstractField {

	static final String separator = ".";

	public AbstractField as(String alias) {
		return StringUtils.isNotBlank(alias) ? new Alias(this, alias) : this;
	}

	public Column concat(Field field) {
		return new Concat(this, field);
	}

}
