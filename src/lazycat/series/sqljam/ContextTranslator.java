package lazycat.series.sqljam;

import java.util.ArrayList;
import java.util.List;

import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.PrimaryKeyDefinition;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * ContextTranslator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class ContextTranslator implements Translator {

	private final Context delegate;

	public ContextTranslator(Context delegate) {
		this.delegate = delegate;
	}

	public TableDefinition getTableDefinition(Configuration configuration) {
		return getTableDefinition(null, configuration);
	}

	public TableDefinition getTableDefinition(String tableAlias, Configuration configuration) {
		Class<?> mappedClass = delegate.findMappedClass(tableAlias, configuration);
		return configuration.getTableDefinition(mappedClass);
	}

	public boolean isPrimaryKey(String tableAlias, String property, Configuration configuration) {
		TableDefinition tableDefinition = getTableDefinition(tableAlias, configuration);
		return tableDefinition.isPrimaryKey(property);
	}

	public String getTableName(String tableAlias, Configuration configuration) {
		Class<?> mappedClass = delegate.findMappedClass(tableAlias, configuration);
		if (mappedClass == null) {
			return null;
		}
		TableDefinition td = configuration.getTableDefinition(mappedClass);
		return td.getTableName();
	}

	public String getColumnName(String tableAlias, String property, Configuration configuration) {
		Class<?> mappedClass = delegate.findMappedClass(tableAlias, configuration);
		if (mappedClass == null) {
			return null;
		}
		ColumnDefinition cd = configuration.getColumnDefinition(mappedClass, property);
		return cd != null ? cd.getColumnName() : null;
	}

	public String getPrimaryKeyName(String tableAlias, Configuration configuration) {
		ColumnDefinition[] definitions = getPrimaryKeys(tableAlias, configuration);
		return definitions != null ? definitions[0].getColumnName() : null;
	}

	public String[] getPrimaryKeyNames(String tableAlias, Configuration configuration) {
		List<String> columns = new ArrayList<String>();
		for (ColumnDefinition definition : getPrimaryKeys(tableAlias, configuration)) {
			columns.add(definition.getColumnName());
		}
		return columns.toArray(new String[0]);
	}

	public ColumnDefinition[] getPrimaryKeys(String tableAlias, Configuration configuration) {
		Class<?> mappedClass = delegate.findMappedClass(tableAlias, configuration);
		if (mappedClass == null) {
			return null;
		}
		TableDefinition td = configuration.getTableDefinition(mappedClass);
		PrimaryKeyDefinition[] definitions = td.getPrimaryKeyDefinitions();
		if (definitions != null) {
			ColumnDefinition[] results = new ColumnDefinition[definitions.length];
			for (int i = 0; i < definitions.length; i++) {
				results[i] = definitions[i].getColumnDefinition();
			}
			return results;
		}
		return null;
	}

	public ColumnDefinition[] getColumns(String tableAlias, Configuration configuration) {
		Class<?> mappedClass = delegate.findMappedClass(tableAlias, configuration);
		if (mappedClass == null) {
			return null;
		}
		TableDefinition td = configuration.getTableDefinition(mappedClass);
		return td.getColumnDefinitions();
	}

	public boolean hasPrimaryKey(String tableAlias, Configuration configuration) {
		Class<?> mappedClass = delegate.findMappedClass(tableAlias, configuration);
		if (mappedClass == null) {
			return false;
		}
		TableDefinition td = configuration.getTableDefinition(mappedClass);
		return td.hasPrimaryKey();
	}

	public String getTableAlias() {
		return delegate.getTableAlias();
	}

}
