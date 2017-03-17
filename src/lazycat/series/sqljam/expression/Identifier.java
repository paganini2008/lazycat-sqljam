package lazycat.series.sqljam.expression;

import lazycat.series.beans.Property;
import lazycat.series.lang.Assert;
import lazycat.series.lang.StringUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.IdentifierNullFault;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.relational.ColumnDefinition;

/**
 * Identifier
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class Identifier implements Expression {

	private final Object object;
	private final String tableAlias;

	public Identifier(Object object, String tableAlias) {
		Assert.isNull(object, "Null example");
		this.object = object;
		this.tableAlias = tableAlias;
	}

	public static Identifier create(Object object) {
		return new Identifier(object, null);
	}

	public String getText(Translator translator, Configuration configuration) {
		String tableAlias = translator.getTableAlias(this.tableAlias);
		String prefix = StringUtils.isNotBlank(tableAlias) ? tableAlias + "." : "";
		StringBuilder sql = new StringBuilder();
		ColumnDefinition[] definitions = translator.getPrimaryKeys(tableAlias, configuration.getMetaData());
		for (int i = 0, l = definitions.length; i < l; i++) {
			sql.append(prefix).append(definitions[i].getColumnName()).append("=?");
			if (i != l - 1) {
				sql.append(" and ");
			}
		}
		return sql.toString();
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		ColumnDefinition[] definitions = translator.getPrimaryKeys(tableAlias, configuration.getMetaData());
		for (ColumnDefinition cd : definitions) {
			Object parameter = getPropertyValue(cd);
			if (parameter == null) {
				throw new IdentifierNullFault();
			}
			parameterCollector.setParameter(parameter, cd.getJdbcType());
		}
	}

	protected Object getPropertyValue(ColumnDefinition cd) {
		return Property.getProperty(cd.getMappedProperty(), (Class<?>) cd.getJavaType()).extract(object);
	}

}
