package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;

/**
 * StandardArithmeticalColumn
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class StandardArithmeticalColumn extends ArithmeticalColumn {

	private final Column left;
	private final Column right;
	private final ArithmeticalOperator op;

	public StandardArithmeticalColumn(Column left, Column right, ArithmeticalOperator op) {
		this.left = left;
		this.right = right;
		this.op = op;
	}

	public StandardArithmeticalColumn(String property, String anotherProperty, ArithmeticalOperator op) {
		this(property, new StandardColumn(property), op);
	}

	public StandardArithmeticalColumn(String property, Column column, ArithmeticalOperator op) {
		this(new StandardColumn(property), column, op);
	}

	public StandardArithmeticalColumn(ArithmeticalColumn column, String property, ArithmeticalOperator op) {
		this(column, new StandardColumn(property), op);
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return op.getText(configuration.getJdbcAdmin().getFeature(), left.getText(session, translator, configuration),
				right.getText(session, translator, configuration));
	}

}
