package lazycat.series.sqljam;

import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * A translator serves to this query
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Translator {

	TableDefinition getTableDefinition(Configuration configuration);

	TableDefinition getTableDefinition(String tableAlias, Configuration configuration);

	String getTableName(String tableAlias, Configuration configuration);

	String getColumnName(String tableAlias, String mappedProperty, Configuration configuration);

	String getPrimaryKeyName(String tableAlias, Configuration configuration);

	String[] getPrimaryKeyNames(String tableAlias, Configuration configuration);

	boolean isPrimaryKey(String tableAlias, String mappedProperty, Configuration configuration);

	boolean hasPrimaryKey(String tableAlias, Configuration configuration);

	ColumnDefinition[] getPrimaryKeys(String tableAlias, Configuration configuration);

	ColumnDefinition[] getColumns(String tableAlias, Configuration configuration);

	String getTableAlias();

}
