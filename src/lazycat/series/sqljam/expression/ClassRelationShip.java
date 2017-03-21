package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.tag.RelationShip;
import lazycat.series.sqljam.relational.ForeignKeyDefinition;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * RelationShip
 * 
 * @author Fred Feng
 * @version 1.0
 */
@RelationShip
public class ClassRelationShip implements Expression {

	private final Class<?> left;
	private final String leftAlias;
	private final Class<?> right;
	private final String rightAlias;

	public ClassRelationShip(Class<?> left, String leftAlias, Class<?> right, String rightAlias) {
		this.left = left;
		this.leftAlias = leftAlias;
		this.right = right;
		this.rightAlias = rightAlias;
	}

	public String getText(Translator translator, Configuration configuration) {
		StringBuilder sql = new StringBuilder();
		TableDefinition leftTable = configuration.getMetaData().getTable(left);
		TableDefinition rightTable = configuration.getMetaData().getTable(right);
		ForeignKeyDefinition[] definitions = leftTable.getForeignKeyDefinitions(right);
		if (definitions != null) {
			int index = 0;
			for (ForeignKeyDefinition definition : definitions) {
				sql.append(leftAlias).append(".");
				sql.append(definition.getColumnDefinition().getColumnName());
				sql.append("=");
				sql.append(rightAlias).append(".");
				sql.append(rightTable.getColumn(definition.getRefMappedProperty()).getColumnName());
				if (index++ != definitions.length - 1) {
					sql.append(" and ");
				}
			}
		}
		return sql.toString();
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
