package lazycat.series.sqljam.field;

/**
 * ArithmeticalColumn
 * 
 * @author Fred Feng
 * @version 1.0
 */
public abstract class MathColumn extends Column {

	public MathColumn add(Number number) {
		return add(new NumberData(number));
	}

	public MathColumn add(String property) {
		return add(new StandardColumn(property));
	}

	public MathColumn add(Column column) {
		return new StandardMathColumn(this, column, MathOperator.ADD);
	}

	public MathColumn subtract(Number number) {
		return subtract(new NumberData(number));
	}

	public MathColumn subtract(String property) {
		return subtract(new StandardColumn(property));
	}

	public MathColumn subtract(Column column) {
		return new StandardMathColumn(this, column, MathOperator.SUBTRACT);
	}

	public MathColumn multiply(Number number) {
		return multiply(new NumberData(number));
	}

	public MathColumn multiply(String property) {
		return multiply(new StandardColumn(property));
	}

	public MathColumn multiply(Column column) {
		return new StandardMathColumn(this, column, MathOperator.MULTIPLY);
	}

	public MathColumn divide(Number number) {
		return divide(new NumberData(number));
	}

	public MathColumn divide(String property) {
		return divide(new StandardColumn(property));
	}

	public MathColumn divide(Column column) {
		return new StandardMathColumn(this, column, MathOperator.DIVIDE);
	}

	public MathColumn modulo(Number number) {
		return modulo(new NumberData(number));
	}

	public MathColumn modulo(String property) {
		return modulo(new StandardColumn(property));
	}

	public MathColumn modulo(Column column) {
		return new StandardMathColumn(this, column, MathOperator.MODULO);
	}

	public MathColumn bitAnd(String property) {
		return bitAnd(new StandardColumn(property));
	}

	public MathColumn bitAnd(Column column) {
		return new StandardMathColumn(this, column, MathOperator.BIT_AND);
	}

	public MathColumn bitOr(String property) {
		return bitOr(new StandardColumn(property));
	}

	public MathColumn bitOr(Column column) {
		return new StandardMathColumn(this, column, MathOperator.BIT_OR);
	}

	public MathColumn bitXor(String property) {
		return bitXor(new StandardColumn(property));
	}

	public MathColumn bitXor(Column column) {
		return new StandardMathColumn(this, column, MathOperator.BIT_XOR);
	}

}
