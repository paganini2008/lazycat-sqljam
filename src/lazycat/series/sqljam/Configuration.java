package lazycat.series.sqljam;

import java.util.List;

import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.SchemaEditor;
import lazycat.series.sqljam.relational.TableDefinition;
import lazycat.series.sqljam.relational.TableEditor;

/**
 * Configuration
 * 
 * @author Fred Feng
 * @version 1.0
 */
public interface Configuration {

	List<Class<?>> getAllMappedClasses();

	void scanPackages(String packageName);

	TableEditor mapClass(Class<?> mappedClass);

	SchemaEditor getSchemaEditor(String schema);

	TableEditor getTableEditor(Class<?> mappedClass);

	TableDefinition getTableDefinition(Class<?> mappedClass);

	ColumnDefinition getColumnDefinition(Class<?> mappedClass, String propertyName);

	boolean hasMapped(Class<?> mappedClass);

	JdbcAdmin getJdbcAdmin();

}