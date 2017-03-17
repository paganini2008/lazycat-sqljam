package lazycat.series.sqljam.update;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ContextTranslator;
import lazycat.series.sqljam.MetaData;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.feature.Feature;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * InsertBuilder
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class InsertBuilder implements SqlBuilder {

	private final Class<?> mappedClass;
	private final Translator translator;

	InsertBuilder(Session session, Class<?> mappedClass) {
		this.mappedClass = mappedClass;
		this.translator = new ContextTranslator(session, this);
	}

	Expression values;

	public String getText(Configuration configuration) {
		TableDefinition tableDefinition = configuration.getMetaData().getTable(mappedClass);
		StringBuilder text = new StringBuilder();
		final Feature feature = configuration.getFeature();
		text.append(feature.insert(tableDefinition.getTableName()));
		text.append(values.getText(translator, configuration));
		return text.toString();
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		values.setParameter(translator, parameterCollector, configuration);
	}

	public Class<?> findMappedClass(String tableAlias, MetaData metaData) {
		return mappedClass;
	}

	public String getTableAlias() {
		return null;
	}

}
