package lazycat.series.sqljam;

import java.util.List;

import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.TableDefinition;
import lazycat.series.sqljam.relational.TableEditor;

/**
 * Describe class metadata
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface MetaData {

	MetaData scanPackage(String packageName);

	MetaData mapClass(Class<?> mappedClass);

	TableEditor addTable(Class<?> mappedClass, String catalog, String schema, String tableName);

	TableDefinition getTable(Class<?> mappedClass);

	ColumnDefinition getColumn(Class<?> mappedClass, String propertyName);

	boolean hasMapped(Class<?> mappedClass);

	List<Class<?>> getAllMappedClasses();

	List<Class<?>> getReferences(Class<?> mappedClass);

}