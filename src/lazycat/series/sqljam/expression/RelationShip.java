package lazycat.series.sqljam.expression;

import java.util.ArrayList;
import java.util.List;

import lazycat.series.collection.CollectionUtils;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.TranslatorException;
import lazycat.series.sqljam.relational.ForeignKeyDefinition;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * RelationShip
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class RelationShip implements Expression {

	private final Class<?> mappedClass;
	private final String rightTableAlias;

	public RelationShip(Class<?> mappedClass, String tableAlias) {
		this.mappedClass = mappedClass;
		this.rightTableAlias = tableAlias;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		TableDefinition leftTable = translator.getTableDefinition(configuration);
		String leftTableAlias = translator.getTableAlias();
		ForeignKeyDefinition[] fkDefinitions = leftTable.getForeignKeyDefinitions();
		if (fkDefinitions != null) {
			List<String> list = new ArrayList<String>();
			for (ForeignKeyDefinition fkDefinition : fkDefinitions) {
				list.add(getEquationText(leftTableAlias, fkDefinition, configuration));
			}
			return CollectionUtils.join(list, " and ");
		}
		throw new TranslatorException("");
	}

	private String getEquationText(String leftTableAlias, ForeignKeyDefinition fkDefinition, Configuration configuration) {
		StringBuilder text = new StringBuilder();
		text.append(leftTableAlias).append(".");
		text.append(fkDefinition.getColumnDefinition().getColumnName());
		text.append("=");
		TableDefinition rightTable = configuration.getTableDefinition(mappedClass);
		text.append(rightTableAlias).append(".");
		text.append(rightTable.getColumnDefinition(fkDefinition.getRefMappedProperty()).getColumnName());
		return text.toString();
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
	}

}
