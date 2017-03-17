package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.TranslatorException;
import lazycat.series.sqljam.relational.ColumnDefinition;

/**
 * JoinExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class JoinExpression implements Expression {

	private final String leftTableAlias;
	private final String rightTableAlias;
	private final ComparisonOperator op;

	public JoinExpression(String leftTableAlias, String rightTableAlias) {
		this(leftTableAlias, rightTableAlias, ComparisonOperator.EQ);
	}

	public JoinExpression(String leftTableAlias, String rightTableAlias, ComparisonOperator op) {
		this.leftTableAlias = leftTableAlias;
		this.rightTableAlias = rightTableAlias;
		this.op = op;
	}

	public String getText(Translator translator, Configuration configuration) {
		ColumnDefinition[] leftDefinitions = translator.getPrimaryKeys(leftTableAlias, configuration.getMetaData());
		ColumnDefinition[] rightDefinitions = translator.getPrimaryKeys(rightTableAlias, configuration.getMetaData());
		int leftLength = leftDefinitions != null ? leftDefinitions.length : 0;
		int rightLength = rightDefinitions != null ? rightDefinitions.length : 0;
		if (leftLength > 0 && rightLength > 0) {
			StringBuffer sql = new StringBuffer();
			for (int i = 0, l = Math.min(leftLength, rightLength); i < l; i++) {
				ColumnDefinition leftCd = leftDefinitions[i];
				ColumnDefinition rightCd = rightDefinitions[i];
				sql.append(leftCd.getColumnName())
						.append(op.getText(configuration.getFeature(), leftCd.getColumnName(), rightCd.getColumnName()));
				if (i != l - 1) {
					sql.append(" and ");
				}
			}
			return sql.toString();
		}
		throw new TranslatorException("ID numbers doesn't match.");
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
