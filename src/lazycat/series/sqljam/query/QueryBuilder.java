package lazycat.series.sqljam.query;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ContextTranslator;
import lazycat.series.sqljam.MetaData;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.expression.SelectAll;
import lazycat.series.sqljam.feature.Feature;
import lazycat.series.sqljam.update.SqlBuilder;

/**
 * QueryBuilder
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class QueryBuilder implements SqlBuilder {

	From query;
	Join join;
	Expression field;
	Expression where;
	Expression group;
	Expression having;
	Expression order;

	private final Translator translator;

	QueryBuilder(Session session, Class<?> mappedClass, String tableAlias) {
		this.query = new SimpleFrom(mappedClass, tableAlias);
		this.translator = new ContextTranslator(session, this);
	}

	public QueryBuilder(Session session, From source, String tableAlias) {
		this.query = new View(source, tableAlias);
		this.translator = new ContextTranslator(session, this);
	}

	public String getTableAlias() {
		return query.getTableAlias();
	}

	public Class<?> findMappedClass(String tableAlias, MetaData metaData) {
		Class<?> find = query.findMappedClass(tableAlias, metaData);
		if (find == null) {
			find = join != null ? join.findMappedClass(tableAlias, metaData) : null;
		}
		return find;
	}

	public String getText(Configuration configuration) {
		final Feature feature = configuration.getFeature();
		StringBuilder text = new StringBuilder();
		text.append(feature.select(false));
		if (field == null) {
			field = new SelectAll();
		}
		text.append(field.getText(translator, configuration));
		text.append(feature.from(query.getText(configuration)));
		if (where != null) {
			text.append(feature.where(where.getText(translator, configuration)));
		}
		if (join != null) {
			text.append(join.getText(translator, configuration));
		}
		if (group != null) {
			text.append(feature.groupBy(group.getText(translator, configuration)));
			if (having != null) {
				text.append(feature.having(having.getText(translator, configuration)));
			}
		}
		if (order != null) {
			text.append(feature.order(order.getText(translator, configuration)));
		}
		return text.toString();
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		query.setParameters(parameterCollector, configuration);
		if (field != null) {
			field.setParameter(translator, parameterCollector, configuration);
		}
		if (where != null) {
			where.setParameter(translator, parameterCollector, configuration);
		}
		if (join != null) {
			join.setParameter(translator, parameterCollector, configuration);
		}
		if (group != null) {
			group.setParameter(translator, parameterCollector, configuration);
			if (having != null) {
				having.setParameter(translator, parameterCollector, configuration);
			}
		}
	}

}
