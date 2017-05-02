package lazycat.series.sqljam.update;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ContextTranslator;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.expression.Field;
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
		this.translator = new ContextTranslator(this);
		this.source = new SimpleFrom(mappedClass, tableAlias);
		this.session = session;
	}

	private final Translator translator;
	private final Session session;

	Field self;
	From source;
	Join join;
	Expression where;

	public String getText(Configuration configuration) {
		final Feature feature = configuration.getJdbcAdmin().getFeature();
		StringBuilder text = new StringBuilder();
		text.append(feature.delete());
		if (self != null) {
			text.append(self.getText(session, translator, configuration));
		}
		text.append(feature.from(source.getText(configuration)));
		if (join != null) {
			text.append(join.getText(session, translator, configuration));
		}
		if (where != null) {
			text.append(feature.where(where.getText(session, translator, configuration)));
		}
		return text.toString();
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		source.setParameters(parameterCollector, configuration);
		if (join != null) {
			join.setParameter(session, translator, parameterCollector, configuration);
		}
		if (where != null) {
			where.setParameter(session, translator, parameterCollector, configuration);
		}
	}

	public Class<?> findMappedClass(String tableAlias, Configuration configuration) {
		return source.findMappedClass(tableAlias, configuration);
	}

	public String getTableAlias() {
		return source.getTableAlias();
	}

	public SqlBuilder copy() {
		return null;
	}

}
