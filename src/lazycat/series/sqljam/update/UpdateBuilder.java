package lazycat.series.sqljam.update;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ContextTranslator;
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

	private final Session session;
	private final Translator translator;

	UpdateBuilder(Session session, Class<?> mappedClass, String tableAlias) {
		this.session = session;
		this.source = new SimpleFrom(mappedClass, tableAlias);
		this.translator = new ContextTranslator(this);
	}

	public String getText(Configuration configuration) {
		final Feature feature = configuration.getJdbcAdmin().getFeature();
		StringBuilder text = new StringBuilder();
		text.append(feature.update(source.getText(configuration)));
		text.append(set.getText(session, translator, configuration));
		if (where != null) {
			text.append(feature.where(where.getText(session, translator, configuration)));
		}
		return text.toString();
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		source.setParameters(parameterCollector, configuration);
		set.setParameter(session, translator, parameterCollector, configuration);
		if (where != null) {
			where.setParameter(session, translator, parameterCollector, configuration);
		}
	}

	public Class<?> findMappedClass(String tableAlias, Configuration metaData) {
		return source.findMappedClass(tableAlias, metaData);
	}

	public String getTableAlias() {
		return source.getTableAlias();
	}

	public SqlBuilder copy() {
		return null;
	}

}
