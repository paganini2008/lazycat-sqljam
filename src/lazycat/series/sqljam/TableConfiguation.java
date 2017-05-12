package lazycat.series.sqljam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lazycat.series.io.ClassFinder;
import lazycat.series.io.Handler;
import lazycat.series.lang.Assert;
import lazycat.series.lang.StringUtils;
import lazycat.series.logger.Log;
import lazycat.series.logger.LogFactory;
import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.SchemaEditor;
import lazycat.series.sqljam.relational.SchemaEditorImpl;
import lazycat.series.sqljam.relational.TableDefinition;
import lazycat.series.sqljam.relational.TableEditor;
import lazycat.series.sqljam.relational.TableEditorImpl;

/**
 * TableMetadataImpl
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class TableConfiguation implements Configuration {

	private static final Log logger = LogFactory.getLog(StandardJdbcAdmin.class);

	private final Map<String, SchemaEditor> schemaEditors = new HashMap<String, SchemaEditor>();
	private final Map<Class<?>, TableEditor> tableEditors = new HashMap<Class<?>, TableEditor>();
	private final JdbcAdmin jdbcAdmin;

	public TableConfiguation(JdbcAdmin jdbcAdmin) {
		this.jdbcAdmin = jdbcAdmin;
	}

	public List<Class<?>> getAllMappedClasses() {
		return new ArrayList<Class<?>>(tableEditors.keySet());
	}

	public TableDefinition getTableDefinition(Class<?> mappedClass) {
		TableEditor tableEditor = tableEditors.get(mappedClass);
		if (tableEditor == null) {
			mapClass(mappedClass);
			jdbcAdmin.resolve(mappedClass);
			tableEditor = tableEditors.get(mappedClass);
		}
		return tableEditor.getTableDefinition();
	}

	public boolean hasMapped(Class<?> mappedClass) {
		return tableEditors.containsKey(mappedClass);
	}

	public ColumnDefinition getColumnDefinition(Class<?> mappedClass, String propertyName) {
		TableDefinition tableDefinition = getTableDefinition(mappedClass);
		return tableDefinition != null ? tableDefinition.getColumnDefinition(propertyName) : null;
	}

	public SchemaEditor getSchemaEditor(String schema) {
		if (StringUtils.isBlank(schema)) {
			schema = null;
		}
		SchemaEditor editor = schemaEditors.get(schema);
		if (editor == null) {
			schemaEditors.put(schema, new SchemaEditorImpl(schema));
			editor = schemaEditors.get(schema);
		}
		return editor;
	}

	public TableEditor getTableEditor(Class<?> mappedClass) {
		TableEditor tableEditor = tableEditors.get(mappedClass);
		if (tableEditor == null) {
			tableEditors.put(mappedClass, new TableEditorImpl(mappedClass, jdbcAdmin));
			tableEditor = tableEditors.get(mappedClass);
		}
		return tableEditor;
	}

	public TableEditor mapClass(Class<?> mappedClass) {
		TableEditor tableEditor = tableEditors.get(mappedClass);
		if (tableEditor == null) {
			tableEditors.put(mappedClass, new AnnotatedTableEditor(mappedClass, jdbcAdmin));
			tableEditor = tableEditors.get(mappedClass);
		}
		return tableEditor;
	}

	public void scanPackages(String packageName) {
		Assert.hasNoText(packageName, "Package names is empty.");
		for (String pName : packageName.split(";")) {
			try {
				ClassFinder.lookup(pName.trim(), true, new Handler() {
					public void publish(String fileName, String className) {
						Class<?> mappedClass;
						try {
							mappedClass = Class.forName(className);
						} catch (ClassNotFoundException e) {
							throw new MappingException(e.getMessage(), e);
						}
						try {
							mapClass(mappedClass);
						} catch (MappingException e) {
							logger.warn(e.getMessage());
						}
					}
				});
			} catch (IOException e) {
				throw new MappingException("Error in scanning package.", e);
			}
		}
	}

	public JdbcAdmin getJdbcAdmin() {
		return jdbcAdmin;
	}

}
