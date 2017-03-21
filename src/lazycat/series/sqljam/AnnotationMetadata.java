package lazycat.series.sqljam;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import lazycat.series.io.ClassFinder;
import lazycat.series.io.Handler;
import lazycat.series.lang.Assert;
import lazycat.series.lang.StringUtils;
import lazycat.series.logger.LazyLogger;
import lazycat.series.logger.LoggerFactory;
import lazycat.series.reflect.FieldUtils;
import lazycat.series.sqljam.annotation.Column;
import lazycat.series.sqljam.annotation.ForeignKey;
import lazycat.series.sqljam.annotation.Identifier;
import lazycat.series.sqljam.annotation.PrimaryKey;
import lazycat.series.sqljam.annotation.Sequence;
import lazycat.series.sqljam.annotation.Table;
import lazycat.series.sqljam.annotation.UniqueKey;
import lazycat.series.sqljam.relational.ColumnDefinition;
import lazycat.series.sqljam.relational.ColumnEditor;
import lazycat.series.sqljam.relational.ForeignKeyDefinition;
import lazycat.series.sqljam.relational.ForeignKeyEditor;
import lazycat.series.sqljam.relational.PrimaryKeyEditor;
import lazycat.series.sqljam.relational.SchemaEditor;
import lazycat.series.sqljam.relational.SchemaEditorImpl;
import lazycat.series.sqljam.relational.TableDefinition;
import lazycat.series.sqljam.relational.TableEditor;
import lazycat.series.sqljam.relational.TableEditorImpl;
import lazycat.series.sqljam.relational.UniqueKeyEditor;

/**
 * AnnotationMetadata
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class AnnotationMetadata implements MetaData {

	private static final LazyLogger logger = LoggerFactory.getLogger(AnnotationMetadata.class);

	private final Map<String, Map<String, SchemaEditor>> schemaEditorRegistry = new HashMap<String, Map<String, SchemaEditor>>();
	private final Map<Class<?>, TableEditor> tableEditorRegistry = new HashMap<Class<?>, TableEditor>();

	private final Observable newMetaDataWatcher = new Observable();

	public SchemaEditor getSchemaEditor(String schema) {
		return getSchemaEditor(null, schema);
	}

	public SchemaEditor getSchemaEditor(String catalog, String schema) {
		if (StringUtils.isBlank(catalog)) {
			catalog = "";
		}
		Map<String, SchemaEditor> m = schemaEditorRegistry.get(catalog);
		if (m == null) {
			schemaEditorRegistry.put(catalog, new HashMap<String, SchemaEditor>());
			m = schemaEditorRegistry.get(catalog);
		}
		SchemaEditor schemaEditor = m.get(schema);
		if (schemaEditor == null) {
			m.put(schema, new SchemaEditorImpl(catalog, schema));
			schemaEditor = m.get(schema);
		}
		return schemaEditor;
	}

	public TableEditor addTable(Class<?> mappedClass, String catalog, String schema, String tableName) {
		if (hasMapped(mappedClass)) {
			throw new MappingException(mappedClass.getName() + " has been mapped.");
		}
		SchemaEditor schemaEditor = getSchemaEditor(catalog, schema);
		tableEditorRegistry.put(mappedClass, new TableEditorImpl(schemaEditor, mappedClass, tableName));
		return tableEditorRegistry.get(mappedClass);
	}

	public MetaData scanPackage(String packageName) {
		Assert.hasNoText(packageName, "Package name is empty.");
		for (String pName : packageName.split(";")) {
			try {
				ClassFinder.lookup(pName.trim(), true, new Handler() {
					public void publish(String fileName, String className) {
						Class<?> mappedClass;
						try {
							mappedClass = Class.forName(className);
						} catch (ClassNotFoundException e) {
							throw new MappingException(e);
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
		return this;
	}

	public synchronized MetaData mapClass(Class<?> mappedClass) {
		if (!mappedClass.isAnnotationPresent(Table.class)) {
			throw new MappingException("Unrecognized class '" + mappedClass.getName() + "' and please set annotation on the class head.");
		}
		Table annotation = mappedClass.getAnnotation(Table.class);
		TableEditor tableEditor = addTable(mappedClass, annotation.catalog(), annotation.schema(), annotation.name());
		tableEditor.setAutoDdl(annotation.autoDdl());
		tableEditor.setComment(annotation.comment());
		tableEditor.setDdlContainsConstraint(annotation.ddlContainsConstraint());
		for (Iterator<Field> iter = FieldUtils.fieldIterator(mappedClass); iter.hasNext();) {
			Field field = iter.next();
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				ColumnEditor columnEditor = tableEditor.addColumn(field.getName(), field.getType(),
						StringUtils.isNotBlank(column.name()) ? column.name() : StringUtils.toSegmentCase(field.getName(), "_"),
						column.jdbcType());
				mapColumn(columnEditor, column);
				mapOther(tableEditor, columnEditor, field);
			}
		}
		return this;
	}

	protected void mapColumn(ColumnEditor columnEditor, Column column) {
		columnEditor.setLength(column.length());
		columnEditor.setPrecision(column.precision());
		columnEditor.setScale(column.scale());
		columnEditor.setDefaultValue(column.defaultValue());
		columnEditor.setNullable(column.nullable());
		columnEditor.setAutoIncrement(column.autoIncrement());
		columnEditor.setUnsigned(column.unsigned());
		columnEditor.setComment(column.comment());
		columnEditor.setInsertSql(column.insertSql());
		columnEditor.setColumnScript(column.value());
	}

	protected void mapOther(TableEditor tableEditor, ColumnEditor columnEditor, Field field) {
		if (field.isAnnotationPresent(Sequence.class)) {
			Sequence sequence = field.getAnnotation(Sequence.class);
			tableEditor.useSequence(field.getName(), sequence.value());
		}
		if (field.isAnnotationPresent(Identifier.class)) {
			Identifier identifier = field.getAnnotation(Identifier.class);
			tableEditor.useIdentifierGenerator(field.getName(), identifier.value());
		}
		if (field.isAnnotationPresent(PrimaryKey.class)) {
			PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
			columnEditor.setNullable(false);
			PrimaryKeyEditor primaryKeyEditor = columnEditor.asPrimaryKey();
			primaryKeyEditor.setConstraintName(primaryKey.name());
			primaryKeyEditor.setPosition(primaryKey.position());
		}
		if (field.isAnnotationPresent(UniqueKey.class)) {
			if (tableEditor.getTableDefinition().isPrimaryKey(columnEditor.getColumnDefinition().getMappedProperty())) {
				throw new MappingException("Duplicated unique key. MappedProperty: " + columnEditor.getColumnDefinition().getMappedProperty()
						+ ", ColumnName: " + columnEditor.getColumnDefinition().getColumnName());
			}
			UniqueKey uniqueKey = field.getAnnotation(UniqueKey.class);
			UniqueKeyEditor uniqueKeyEditor = columnEditor.asUniqueKey();
			uniqueKeyEditor.setConstraintName(uniqueKey.name());
			uniqueKeyEditor.setPosition(uniqueKey.position());
		}
		if (field.isAnnotationPresent(ForeignKey.class)) {
			ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
			ForeignKeyEditor foreignKeyEditor = columnEditor.asForeignKey(foreignKey.refMappedClass(), foreignKey.refMappedProperty());
			foreignKeyEditor.setConstraintName(foreignKey.name());
			foreignKeyEditor.setPosition(foreignKey.position());
			foreignKeyEditor.setRequired(foreignKey.required());
			foreignKeyEditor.setOnUpdate(foreignKey.onUpdate());
			foreignKeyEditor.setOnDelete(foreignKey.onDelete());
		}
	}

	public synchronized TableDefinition getTable(Class<?> mappedClass) {
		TableEditor tableEditor = tableEditorRegistry.get(mappedClass);
		if (tableEditor == null) {
			newMetaDataWatcher.notifyObservers(mappedClass);
			tableEditor = tableEditorRegistry.get(mappedClass);
		}
		return tableEditor.getTableDefinition();
	}

	public synchronized boolean hasMapped(Class<?> mappedClass) {
		return tableEditorRegistry.containsKey(mappedClass);
	}

	public synchronized ColumnDefinition getColumn(Class<?> mappedClass, String propertyName) {
		TableDefinition tableDefinition = getTable(mappedClass);
		return tableDefinition != null ? tableDefinition.getColumn(propertyName) : null;
	}

	public synchronized List<Class<?>> getAllMappedClasses() {
		return new ArrayList<Class<?>>(tableEditorRegistry.keySet());
	}

	public List<Class<?>> getReferences(Class<?> mappedClass) {
		List<Class<?>> list = new ArrayList<Class<?>>();
		for (Class<?> cl : getAllMappedClasses()) {
			TableDefinition table = getTable(cl);
			if (table.hasForeignKey()) {
				for (ForeignKeyDefinition fkDefinition : table.getForeignKeyDefinitions()) {
					if (fkDefinition.getRefMappedClass() == mappedClass) {
						if (!list.contains(cl)) {
							list.add(cl);
						}
					}
				}
			}
		}
		return list;
	}

	void watch(Observer o) {
		newMetaDataWatcher.addObserver(o);
	}
}
