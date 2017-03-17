package lazycat.series.sqljam.update;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ContextTranslator;
import lazycat.series.sqljam.MetaData;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.feature.Feature;
import lazycat.series.sqljam.query.From;
import lazycat.series.sqljam.query.SimpleFrom;

/**
 * UpdateBuilder
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class UpdateBuilder implements SqlBuilder {

	From source;
	Expression set;
	Expression where;

	private final Translator translator;

	UpdateBuilder(Session session, Class<?> mappedClass, String tableAlias) {
		this.translator = new ContextTranslator(session, this);
		this.source = new SimpleFrom(mappedClass, tableAlias);
	}

	public String getText(Configuration configuration) {
		final Feature feature = configuration.getFeature();
		StringBuilder text = new StringBuilder();
		text.append(feature.update(source.getText(configuration)));
		text.append(feature.set(set.getText(translator, configuration)));
		if (where != null) {
			text.append(feature.where(where.getText(translator, configuration)));
		}
		return text.toString();
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		source.setParameters(parameterCollector, configuration);
		set.setParameter(translator, parameterCollector, configuration);
		if (where != null) {
			where.setParameter(translator, parameterCollector, configuration);
		}
	}

	public Class<?> findMappedClass(String tableAlias, MetaData metaData) {
		return source.findMappedClass(tableAlias, metaData);
	}

	public String getTableAlias() {
		return source.getTableAlias();
	}

}
