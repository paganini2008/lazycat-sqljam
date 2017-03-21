package lazycat.series.sqljam.update;

import java.util.ArrayList;
import java.util.List;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.expression.Expressions;
import lazycat.series.sqljam.expression.Label;
import lazycat.series.sqljam.expression.ClassRelationShip;
import lazycat.series.sqljam.query.Query;

/**
 * CascadeDelete
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class CascadeDelete implements Delete {

	public CascadeDelete(Session session, Class<?> mappedClass) {
		this(session, mappedClass, "this");
	}

	public CascadeDelete(Session session, Class<?> mappedClass, String tableAlias) {
		this.mappedClass = mappedClass;
		this.last = session.delete(mappedClass);
		List<Class<?>> references = session.getConfiguration().getMetaData().getReferences(mappedClass);
		for (Class<?> reference : references) {
			cascades.add(session.query(mappedClass).relation(new ClassRelationShip(reference, "reference", mappedClass, tableAlias))
					.column(Label.ONE));
		}
		this.session = session;
	}

	private final Delete last;
	private final Class<?> mappedClass;
	private final Session session;
	private final List<Query> cascades = new ArrayList<Query>();

	public int execute() {
		int effected = 0;
		List<Class<?>> references = session.getConfiguration().getMetaData().getReferences(mappedClass);
		for (int i = 0; i < references.size(); i++) {
			effected += session.delete(references.get(i), "reference").filter(Expressions.exists(cascades.get(i))).execute();
		}
		effected += last.execute();
		return effected;
	}

	public Delete self() {
		last.self();
		return this;
	}

	public Delete filter(Expression expression) {
		last.filter(expression);
		for (Query q : cascades) {
			q.filter(expression);
		}
		return this;
	}

	public Delete crossJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		last.crossJoin(mappedClass, tableAlias, on);
		for (Query q : cascades) {
			q.crossJoin(mappedClass, tableAlias, on);
		}
		return this;
	}

	public Delete innerJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		last.innerJoin(mappedClass, tableAlias, on);
		for (Query q : cascades) {
			q.innerJoin(mappedClass, tableAlias, on);
		}
		return this;
	}

	public Delete leftJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		last.leftJoin(mappedClass, tableAlias, on);
		for (Query q : cascades) {
			q.leftJoin(mappedClass, tableAlias, on);
		}
		return this;
	}

	public Delete rightJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		last.rightJoin(mappedClass, tableAlias, on);
		for (Query q : cascades) {
			q.rightJoin(mappedClass, tableAlias, on);
		}
		return this;
	}

	public Delete fullJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		last.fullJoin(mappedClass, tableAlias, on);
		for (Query q : cascades) {
			q.fullJoin(mappedClass, tableAlias, on);
		}
		return this;
	}

	public String getText(Configuration configuration) {
		return last.getText(configuration);
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		last.setParameters(parameterCollector, configuration);
	}

}
