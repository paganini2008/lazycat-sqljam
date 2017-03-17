package lazycat.series.sqljam;

import lazycat.series.sqljam.relational.ColumnDefinition;

public interface Scalar {

	String getTableAlias();

	ColumnDefinition getColumn();

	Class<?> getRequiredType();

}
