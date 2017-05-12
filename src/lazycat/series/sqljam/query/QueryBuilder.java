package lazycat.series.sqljam.query;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ContextTranslator;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.feature.Feature;
import lazycat.series.sqljam.field.All;
import lazycat.series.sqljam.field.Field;
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
	Field columns;
	Expression where;
	Field group;
	Expression having;
	Field order;

	private Translator translator;
	private Session session;

	public QueryBuilder(Session session, Class<?> mappedClass, String tableAlias) {
		this.query = new SimpleFrom(mappedClass, tableAlias);
		this.translator = new ContextTranslator(this);
		this.session = session;
	}

	public QueryBuilder(Session session, From source, String tableAlias) {
		this.query = new View(source, tableAlias);
		this.translator = new ContextTranslator(this);
		this.session = session;
	}

	QueryBuilder() {
	}

	public String getTableAlias() {
		return query.getTableAlias();
	}

	public Class<?> findMappedClass(String tableAlias, Configuration configuration) {
		Class<?> find = query.findMappedClass(tableAlias, configuration);
		if (find == null) {
			find = join != null ? join.findMappedClass(tableAlias, configuration) : null;
		}
		return find;
	}

	public String getText(Configuration configuration) {
		final Feature feature = configuration.getJdbcAdmin().getFeature();
		StringBuilder text = new StringBuilder();
		text.append(feature.select(false));
		if (columns == null) {
			columns = All.THIS;
		}
		text.append(columns.getText(session, translator, configuration));
		text.append(feature.from(query.getText(configuration)));
		if (where != null) {
			text.append(feature.where(where.getText(session, translator, configuration)));
		}
		if (join != null) {
			text.append(join.getText(session, translator, configuration));
		}
		if (group != null) {
			text.append(feature.groupBy(group.getText(session, translator, configuration)));
			if (having != null) {
				text.append(feature.having(having.getText(session, translator, configuration)));
			}
		}
		if (order != null) {
			text.append(feature.order(order.getText(session, translator, configuration)));
		}
		return text.toString();
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		query.setParameters(parameterCollector, configuration);
		if (where != null) {
			where.setParameter(session, translator, parameterCollector, configuration);
		}
		if (join != null) {
			join.setParameter(session, translator, parameterCollector, configuration);
		}
		if (group != null && having != null) {
			having.setParameter(session, translator, parameterCollector, configuration);
		}
	}

	public SqlBuilder copy() {
		QueryBuilder builder = new QueryBuilder();
		builder.columns = columns;
		builder.group = group;
		builder.having = having;
		builder.join = join;
		builder.order = order;
		builder.query = query;
		builder.where = where;
		builder.translator = translator;
		return builder;
	}

}
