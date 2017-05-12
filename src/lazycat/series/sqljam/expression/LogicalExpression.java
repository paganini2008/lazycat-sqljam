package lazycat.series.sqljam.expression;

/**
 * Logical expression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public abstract class LogicalExpression implements Expression {

	/**
	 * And
	 * 
	 * @param expression
	 * @return
	 */
	public LogicalExpression and(Expression expression) {
		return new AndExpression(this, expression);
	}

	/**
	 * Or
	 * 
	 * @param expression
	 * @return
	 */
	public LogicalExpression or(Expression expression) {
		return new OrExpression(this, expression);
	}

	/**
	 * Not
	 * 
	 * @return
	 */
	public LogicalExpression not() {
		return new NotExpression(this);
	}

	/**
	 * Wrap
	 * 
	 * @return
	 */
	public LogicalExpression wrap() {
		return new WrapExpression(this);
	}

}
