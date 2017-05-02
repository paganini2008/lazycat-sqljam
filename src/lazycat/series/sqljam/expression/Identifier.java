package lazycat.series.sqljam.expression;

import lazycat.series.beans.Property;
import lazycat.series.lang.Assert;
import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.IdentifierNullFault;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * Identifier
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Identifier implements Expression {

	private final Object object;
	private final Table table;

	public Identifier(Object object, String alias) {
		Assert.isNull(object, "Null example");
		this.object = object;
		this.table = Table.get(alias);
	}

	public static Identifier create(Object object) {
		return new Identifier(object, null);
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		String tableAlias = table.getText(session, translator, configuration);
		String prefix = StringUtils.isNotBlank(tableAlias) ? tableAlias + "." : "";
		StringBuilder sql = new StringBuilder();
		ColumnDefinition[] definitions = translator.getPrimaryKeys(tableAlias, configuration);
		for (int i = 0, l = definitions.length; i < l; i++) {
			sql.append(prefix).append(definitions[i].getColumnName()).append("=?");
			if (i != l - 1) {
				sql.append(" and ");
			}
		}
		return sql.toString();
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		String tableAlias = table.getText(session, translator, configuration);
		TableDefinition tableDefinition= translator.getTableDefinition(tableAlias, configuration);
		ColumnDefinition[] definitions = tableDefinition.getColumnDefinitions();
		for (ColumnDefinition columnDefinition : definitions) {
			Object parameter = getPropertyValue(columnDefinition);
			if (parameter == null) {
				throw new IdentifierNullFault();
			}
			parameterCollector.setParameter(parameter, columnDefinition.getJdbcType());
		}
	}

	protected Object getPropertyValue(ColumnDefinition cd) {
		return Property.getProperty(cd.getMappedProperty(), (Class<?>) cd.getJavaType()).extract(object);
	}

}
