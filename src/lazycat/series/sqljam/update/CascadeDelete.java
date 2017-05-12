package lazycat.series.sqljam.update;

import java.util.ArrayList;
import java.util.List;

import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.expression.Expression;
import lazycat.series.sqljam.expression.Expressions;
import lazycat.series.sqljam.field.NumberData;
import lazycat.series.sqljam.query.Query;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * CascadeDelete
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class CascadeDelete extends AbstractExecutor implements Delete {

	public CascadeDelete(Session session, Class<?> mappedClass) {
		this(session, mappedClass, "this");
	}

	public CascadeDelete(Session session, Class<?> mappedClass, String tableAlias) {
		super(session);
		this.last = session.delete(mappedClass, tableAlias);
		Configuration configuration = session.getSessionAdmin().getSessionExecutor().getConfiguration();
		TableDefinition tableDefinition = configuration.getTableDefinition(mappedClass);
		for (TableDefinition reference : tableDefinition.getReferences()) {
			deletes.add(new QueryDelete(session, mappedClass, tableAlias, reference.getMappedClass()));
		}
	}

	private final Delete last;
	private final List<Delete> deletes = new ArrayList<Delete>();

	/**
	 * QueryDelete
	 * 
	 * @author Fred Feng
	 * @version 1.0
	 */
	static class QueryDelete implements Delete {

		final Query q;
		final Delete d;

		QueryDelete(Session session, Class<?> mappedClass, String tableAlias, Class<?> reference) {
			this.q = session.query(mappedClass).relate(reference, tableAlias).column(NumberData.ONE);
			this.d = session.delete(reference, "reference").filter(Expressions.exists(q));
		}

		public Delete self(boolean show) {
			d.self(show);
			return null;
		}

		public Delete filter(Expression expression) {
			q.filter(expression);
			return null;
		}

		public Delete crossJoin(Class<?> mappedClass, String tableAlias, Expression on) {
			q.crossJoin(mappedClass, tableAlias, on);
			return null;
		}

		public Delete innerJoin(Class<?> mappedClass, String tableAlias, Expression on) {
			q.innerJoin(mappedClass, tableAlias, on);
			return null;
		}

		public Delete leftJoin(Class<?> mappedClass, String tableAlias, Expression on) {
			q.leftJoin(mappedClass, tableAlias, on);
			return null;
		}

		public Delete rightJoin(Class<?> mappedClass, String tableAlias, Expression on) {
			q.rightJoin(mappedClass, tableAlias, on);
			return null;
		}

		public Delete fullJoin(Class<?> mappedClass, String tableAlias, Expression on) {
			q.fullJoin(mappedClass, tableAlias, on);
			return null;
		}

		public String getText(Configuration configuration) {
			return d.getText(configuration);
		}

		public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
			d.setParameters(parameterCollector, configuration);
		}

		public int execute() {
			throw new UnsupportedOperationException();
		}

		public void executeAsync() {
			throw new UnsupportedOperationException();
		}

	}

	public int execute() {
		int effected = 0;
		for (Delete delete : deletes) {
			session.execute(delete);
		}
		effected += session.execute(this);
		return effected;
	}

	public Delete self(boolean show) {
		for (Delete delete : deletes) {
			delete.self(show);
		}
		last.self(show);
		return this;
	}

	public Delete filter(Expression expression) {
		for (Delete delete : deletes) {
			delete.filter(expression);
		}
		last.filter(expression);
		return this;
	}

	public Delete crossJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		for (Delete delete : deletes) {
			delete.crossJoin(mappedClass, tableAlias, on);
		}
		last.crossJoin(mappedClass, tableAlias, on);
		return this;
	}

	public Delete innerJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		for (Delete delete : deletes) {
			delete.innerJoin(mappedClass, tableAlias, on);
		}
		last.innerJoin(mappedClass, tableAlias, on);
		return this;
	}

	public Delete leftJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		for (Delete delete : deletes) {
			delete.leftJoin(mappedClass, tableAlias, on);
		}
		last.leftJoin(mappedClass, tableAlias, on);
		return this;
	}

	public Delete rightJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		for (Delete delete : deletes) {
			delete.rightJoin(mappedClass, tableAlias, on);
		}
		last.rightJoin(mappedClass, tableAlias, on);
		return this;
	}

	public Delete fullJoin(Class<?> mappedClass, String tableAlias, Expression on) {
		for (Delete delete : deletes) {
			delete.fullJoin(mappedClass, tableAlias, on);
		}
		last.fullJoin(mappedClass, tableAlias, on);
		return this;
	}

	public String getText(Configuration configuration) {
		return last.getText(configuration);
	}

	public void setParameters(ParameterCollector parameterCollector, Configuration configuration) {
		last.setParameters(parameterCollector, configuration);
	}

}
