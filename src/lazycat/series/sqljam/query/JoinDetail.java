package lazycat.series.sqljam.query;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.JoinType;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.feature.Feature;

/**
 * JoinDetail
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class JoinDetail implements Join {

	private final From source;
	private final JoinType joinType;
	private final Expression expression;
	private final Join last;

	public JoinDetail(From source, JoinType joinType, Expression on, Join last) {
		this.source = source;
		this.joinType = joinType;
		this.expression = on;
		this.last = last;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		final Feature feature = configuration.getJdbcAdmin().getFeature();
		String str = " " + joinType.getText(feature,
				feature.on(source.getText(configuration), expression.getText(session, translator, configuration)));
		if (last != null) {
			return last.getText(session, translator, configuration) + str;
		}
		return str;
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		if (last != null) {
			last.setParameter(session, translator, parameterCollector, configuration);
		}
		source.setParameters(parameterCollector, configuration);
		expression.setParameter(session, translator, parameterCollector, configuration);
	}

	public Join join(From source, JoinType joinType, Expression on) {
		return new JoinDetail(source, joinType, on, this);
	}

	public int size() {
		if (last != null) {
			return last.size() + 1;
		}
		return 1;
	}

	public Class<?> findMappedClass(String tableAlias, Configuration metaData) {
		Class<?> result = source.findMappedClass(tableAlias, metaData);
		if (result != null) {
			return result;
		}
		return last != null ? last.findMappedClass(tableAlias, metaData) : null;
	}

	public String getTableAlias() {
		return source.getTableAlias();
	}

}
