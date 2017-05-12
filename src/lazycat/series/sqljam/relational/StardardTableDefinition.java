package lazycat.series.sqljam.relational;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lazycat.series.beans.ToStringBuilder;
import lazycat.series.beans.ToStringBuilder.PrintStyle;
import lazycat.series.sqljam.AutoDdl;
import lazycat.series.sqljam.MappingException;

/**
 * Table Description
 * 
 * @author Fred Feng
 * @see StandardColumnDefinition
 * @see StandardPrimaryKeyDefinition
 */
public class StardardTableDefinition implements TableDefinition {

	private final Class<?> mappedClass;
	private SchemaDefinition schemaDefinition;
	private String tableName;
	private boolean defineConstraintOnCreate;
	private String comment;
	private AutoDdl autoDdl;

	final Map<String, ColumnDefinition> columns = new LinkedHashMap<String, ColumnDefinition>();
	final Map<String, PrimaryKeyDefinition> primaryKeys = new LinkedHashMap<String, PrimaryKeyDefinition>();
	final Map<String, ForeignKeyDefinition> foreignKeys = new LinkedHashMap<String, ForeignKeyDefinition>();
	final Map<String, UniqueKeyDefinition> uniqueKeys = new LinkedHashMap<String, UniqueKeyDefinition>();
	final Map<String, DefaultDefinition> defaults = new LinkedHashMap<String, DefaultDefinition>();
	final Set<TableDefinition> references = new HashSet<TableDefinition>();

	StardardTableDefinition(Class<?> mappedClass) {
		this.mappedClass = mappedClass;
	}

	public void setAutoDdl(AutoDdl autoDdl) {
		this.autoDdl = autoDdl;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public AutoDdl getAutoDdl() {
		return autoDdl;
	}

	public String getComment() {
		return comment;
	}

	public Class<?> getMappedClass() {
		return mappedClass;
	}

	public TableDefinition[] getReferences() {
		return references.toArray(new TableDefinition[0]);
	}

	public String getTableName() {
		return tableName;
	}

	public boolean isDefineConstraintOnCreate() {
		return defineConstraintOnCreate;
	}

	public void setDefineConstraintOnCreate(boolean defineConstraintOnCreate) {
		this.defineConstraintOnCreate = defineConstraintOnCreate;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSchema() {
		return schemaDefinition != null ? schemaDefinition.getSchema() : null;
	}

	public void setSchemaDefinition(SchemaDefinition schemaDefinition) {
		this.schemaDefinition = schemaDefinition;
	}

	public DefaultDefinition getDefaultDefinition(String propertyName) {
		return defaults.get(propertyName);
	}

	public ColumnDefinition getColumnDefinition(String mappedProperty) {
		return columns.get(mappedProperty);
	}

	public boolean hasProperty(String mappedProperty) {
		return columns.containsKey(mappedProperty);
	}

	public ColumnDefinition[] getColumnDefinitions() {
		return columns.values().toArray(new ColumnDefinition[0]);
	}

	public boolean hasPrimaryKey() {
		return primaryKeys.size() > 0;
	}

	public boolean hasForeignKey() {
		return foreignKeys.size() > 0;
	}

	public boolean hasUniqueKey() {
		return uniqueKeys.size() > 0;
	}

	public boolean isPrimaryKey(String mappedProperty) {
		return primaryKeys.containsKey(mappedProperty);
	}

	public PrimaryKeyDefinition getPrimaryKeyDefinition() {
		PrimaryKeyDefinition[] results = getPrimaryKeyDefinitions();
		return results != null ? results[0] : null;
	}

	public boolean isAutoIncrement(String mappedProperty) {
		if (columns.containsKey(mappedProperty)) {
			return columns.get(mappedProperty).isAutoIncrement();
		}
		throw new MappingException("Unknow mappedProperty: " + mappedProperty);
	}

	public UniqueKeyDefinition[] getUniqueKeyDefinitions() {
		if (uniqueKeys.size() > 0) {
			List<UniqueKeyDefinition> list = new ArrayList<UniqueKeyDefinition>(uniqueKeys.values());
			Collections.sort(list);
			return list.toArray(new UniqueKeyDefinition[list.size()]);
		}
		return null;
	}

	public ForeignKeyDefinition[] getForeignKeyDefinitions(Class<?> mappedClass) {
		if (foreignKeys.size() > 0) {
			List<ForeignKeyDefinition> list = new ArrayList<ForeignKeyDefinition>();
			for (ForeignKeyDefinition definition : foreignKeys.values()) {
				if (definition.getRefMappedClass() == mappedClass) {
					list.add(definition);
				}
			}
			Collections.sort(list);
			return list.toArray(new ForeignKeyDefinition[list.size()]);
		}
		return null;
	}

	public ForeignKeyDefinition[] getForeignKeyDefinitions() {
		if (foreignKeys.size() > 0) {
			List<ForeignKeyDefinition> list = new ArrayList<ForeignKeyDefinition>(foreignKeys.values());
			Collections.sort(list);
			return list.toArray(new ForeignKeyDefinition[list.size()]);
		}
		return null;
	}

	public PrimaryKeyDefinition[] getPrimaryKeyDefinitions() {
		if (primaryKeys.size() > 0) {
			List<PrimaryKeyDefinition> list = new ArrayList<PrimaryKeyDefinition>(primaryKeys.values());
			Collections.sort(list);
			return list.toArray(new PrimaryKeyDefinition[list.size()]);
		}
		return null;
	}

	public SchemaDefinition getSchemaDefinition() {
		return schemaDefinition;
	}

	public String toString() {
		return ToStringBuilder.reflectInvokeToString(this, PrintStyle.MULTI_LINE);
	}

}
