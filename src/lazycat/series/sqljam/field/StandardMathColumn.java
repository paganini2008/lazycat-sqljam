package lazycat.series.sqljam.field;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * StandardMathColumn
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class StandardMathColumn extends MathColumn {

	private final Column left;
	private final Column right;
	private final MathOperator op;

	public StandardMathColumn(Column left, Column right, MathOperator op) {
		this.left = left;
		this.right = right;
		this.op = op;
	}

	public StandardMathColumn(String property, String anotherProperty, MathOperator op) {
		this(property, new StandardColumn(property), op);
	}

	public StandardMathColumn(String property, Column column, MathOperator op) {
		this(new StandardColumn(property), column, op);
	}

	public StandardMathColumn(MathColumn column, String property, MathOperator op) {
		this(column, new StandardColumn(property), op);
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return op.getText(configuration.getJdbcAdmin().getFeature(), left.getText(session, translator, configuration),
				right.getText(session, translator, configuration));
	}

}
