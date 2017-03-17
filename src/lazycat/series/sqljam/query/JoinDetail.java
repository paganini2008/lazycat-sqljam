package lazycat.series.sqljam.query;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.JoinType;
import lazycat.series.sqljam.MetaData;
import lazycat.series.sqljam.ParameterCollector;
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

	public String getText(Translator translator, Configuration configuration) {
		final Feature feature = configuration.getFeature();
		String str = " "
				+ joinType.sqlText(feature, feature.on(source.getText(configuration), expression.getText(translator, configuration)));
		if (last != null) {
			return last.getText(translator, configuration) + str;
		}
		return str;
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		if (last != null) {
			last.setParameter(translator, parameterCollector, configuration);
		}
		source.setParameters(parameterCollector, configuration);
		expression.setParameter(translator, parameterCollector, configuration);
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

	public Class<?> findMappedClass(String tableAlias, MetaData metaData) {
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
