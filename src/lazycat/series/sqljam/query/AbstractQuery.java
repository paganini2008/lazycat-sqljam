package lazycat.series.sqljam.query;

/**
 * Abstract Query Clause
 * 
 * @author Fred Feng
 * @version 1.0
 */
public abstract class AbstractQuery extends StandardFrom {

	public AbstractQuery union(From other) {
		return new Union(this, other);
	}

	public AbstractQuery unionAll(From other) {
		return new UnionAll(this, other);
	}

	public AbstractQuery intersect(From other) {
		return new Intersect(this, other);
	}

	public AbstractQuery minus(From other) {
		return new Minus(this, other);
	}

	public String getTableAlias() {
		return null;
	}
}
