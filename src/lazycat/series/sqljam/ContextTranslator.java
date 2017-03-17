package lazycat.series.sqljam;

import java.util.ArrayList;
import java.util.List;

import lazycat.series.lang.StringUtils;
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

	private final Session session;
	private final Context delegate;

	public ContextTranslator(Session session, Context delegate) {
		this.session = session;
		this.delegate = delegate;
	}

	public boolean isPrimaryKey(String tableAlias, String mappedProperty, MetaData metaData) {
		Class<?> mappedClass = delegate.findMappedClass(tableAlias, metaData);
		if (mappedClass == null) {
			return false;
		}
		TableDefinition td = metaData.getTable(mappedClass);
		return td.isPrimaryKey(mappedProperty);
	}

	public String getTableName(String tableAlias, MetaData metaData) {
		Class<?> mappedClass = delegate.findMappedClass(tableAlias, metaData);
		if (mappedClass == null) {
			return null;
		}
		TableDefinition td = metaData.getTable(mappedClass);
		return td.getTableName();
	}

	public String getColumnName(String tableAlias, String mappedProperty, MetaData metaData) {
		Class<?> mappedClass = delegate.findMappedClass(tableAlias, metaData);
		if (mappedClass == null) {
			return null;
		}
		ColumnDefinition cd = metaData.getColumn(mappedClass, mappedProperty);
		return cd != null ? cd.getColumnName() : null;
	}

	public String getPrimaryKeyName(String tableAlias, MetaData metaData) {
		ColumnDefinition[] definitions = getPrimaryKeys(tableAlias, metaData);
		return definitions != null ? definitions[0].getColumnName() : null;
	}

	public String[] getPrimaryKeyNames(String tableAlias, MetaData metaData) {
		List<String> columns = new ArrayList<String>();
		for (ColumnDefinition definition : getPrimaryKeys(tableAlias, metaData)) {
			columns.add(definition.getColumnName());
		}
		return columns.toArray(new String[0]);
	}

	public ColumnDefinition[] getPrimaryKeys(String tableAlias, MetaData metaData) {
		Class<?> mappedClass = delegate.findMappedClass(tableAlias, metaData);
		if (mappedClass == null) {
			return null;
		}
		TableDefinition td = metaData.getTable(mappedClass);
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

	public ColumnDefinition[] getColumns(String tableAlias, MetaData metaData) {
		Class<?> mappedClass = delegate.findMappedClass(tableAlias, metaData);
		if (mappedClass == null) {
			return null;
		}
		TableDefinition td = metaData.getTable(mappedClass);
		return td.getColumnDefinitions();
	}

	public boolean hasPrimaryKey(String tableAlias, MetaData metaData) {
		Class<?> mappedClass = delegate.findMappedClass(tableAlias, metaData);
		if (mappedClass == null) {
			return false;
		}
		TableDefinition td = metaData.getTable(mappedClass);
		return td.hasPrimaryKey();
	}

	public String getTableAlias(String tableAlias) {
		if (StringUtils.isBlank(tableAlias)) {
			return delegate.getTableAlias();
		}
		return tableAlias;
	}

	public Session getCurrentSession() {
		return session;
	}

}
