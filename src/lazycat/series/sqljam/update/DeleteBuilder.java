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
import lazycat.series.sqljam.query.Join;
import lazycat.series.sqljam.query.SimpleFrom;

/**
 * DeleteBuilder
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class DeleteBuilder implements SqlBuilder {

	DeleteBuilder(Session session, Class<?> mappedClass, String tableAlias) {
		this.translator = new ContextTranslator(session, this);
		this.source = new SimpleFrom(mappedClass, tableAlias);
	}

	private final Translator translator;

	Expression alias;
	From source;
	Join join;
	Expression where;

	public String getText(Configuration configuration) {
		final Feature feature = configuration.getFeature();
		StringBuilder text = new StringBuilder();
		text.append(feature.delete());
		if (alias != null) {
			text.append(alias.getText(translator, configuration));
		}
		text.append(feature.from(source.getText(configuration)));
		if (join != null) {
			text.append(join.getText(translator, configuration));
		}
		if (where != null) {
			text.append(feature.where(where.getText(translator, configuration)));
		}
		return text.toString();
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		if (alias != null) {
			alias.setParameter(translator, parameterCollector, configuration);
		}
		source.setParameters(parameterCollector, configuration);
		if (join != null) {
			join.setParameter(translator, parameterCollector, configuration);
		}
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
