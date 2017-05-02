package lazycat.series.sqljam.expression;

/**
 * ArithmeticalColumn
 * 
 * @author Fred Feng
 * @version 1.0
 */
public abstract class ArithmeticalColumn extends Column {

	public ArithmeticalColumn add(Number number) {
		return add(new NumberData(number));
	}

	public ArithmeticalColumn add(String property) {
		return add(new StandardColumn(property));
	}

	public ArithmeticalColumn add(Column column) {
		return new StandardArithmeticalColumn(this, column, ArithmeticalOperator.ADD);
	}

	public ArithmeticalColumn subtract(Number number) {
		return subtract(new NumberData(number));
	}

	public ArithmeticalColumn subtract(String property) {
		return subtract(new StandardColumn(property));
	}

	public ArithmeticalColumn subtract(Column column) {
		return new StandardArithmeticalColumn(this, column, ArithmeticalOperator.SUBTRACT);
	}

	public ArithmeticalColumn multiply(Number number) {
		return multiply(new NumberData(number));
	}

	public ArithmeticalColumn multiply(String property) {
		return multiply(new StandardColumn(property));
	}

	public ArithmeticalColumn multiply(Column column) {
		return new StandardArithmeticalColumn(this, column, ArithmeticalOperator.MULTIPLY);
	}

	public ArithmeticalColumn divide(Number number) {
		return divide(new NumberData(number));
	}

	public ArithmeticalColumn divide(String property) {
		return divide(new StandardColumn(property));
	}

	public ArithmeticalColumn divide(Column column) {
		return new StandardArithmeticalColumn(this, column, ArithmeticalOperator.DIVIDE);
	}

	public ArithmeticalColumn modulo(Number number) {
		return modulo(new NumberData(number));
	}

	public ArithmeticalColumn modulo(String property) {
		return modulo(new StandardColumn(property));
	}

	public ArithmeticalColumn modulo(Column column) {
		return new StandardArithmeticalColumn(this, column, ArithmeticalOperator.MODULO);
	}

	public ArithmeticalColumn bitAnd(String property) {
		return bitAnd(new StandardColumn(property));
	}

	public ArithmeticalColumn bitAnd(Column column) {
		return new StandardArithmeticalColumn(this, column, ArithmeticalOperator.BIT_AND);
	}

	public ArithmeticalColumn bitOr(String property) {
		return bitOr(new StandardColumn(property));
	}

	public ArithmeticalColumn bitOr(Column column) {
		return new StandardArithmeticalColumn(this, column, ArithmeticalOperator.BIT_OR);
	}

	public ArithmeticalColumn bitXor(String property) {
		return bitXor(new StandardColumn(property));
	}

	public ArithmeticalColumn bitXor(Column column) {
		return new StandardArithmeticalColumn(this, column, ArithmeticalOperator.BIT_XOR);
	}

}
