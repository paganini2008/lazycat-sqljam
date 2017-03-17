package lazycat.series.sqljam;

import lazycat.series.sqljam.relational.ColumnDefinition;

/**
 * A translator serves to this query
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Translator {

	String getTableName(String tableAlias, MetaData metaData);

	String getColumnName(String tableAlias, String mappedProperty, MetaData metaData);

	String getPrimaryKeyName(String tableAlias, MetaData metaData);

	String[] getPrimaryKeyNames(String tableAlias, MetaData metaData);

	boolean isPrimaryKey(String tableAlias, String mappedProperty, MetaData metaData);

	boolean hasPrimaryKey(String tableAlias, MetaData metaData);

	ColumnDefinition[] getPrimaryKeys(String tableAlias, MetaData metaData);

	ColumnDefinition[] getColumns(String tableAlias, MetaData metaData);
	
	String getTableAlias(String tableAlias);

	Session getCurrentSession();

}
