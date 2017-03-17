package lazycat.series.sqljam.relational;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lazycat.series.beans.ToStringBuilder;
import lazycat.series.beans.ToStringBuilder.PrintStyle;
import lazycat.series.sqljam.AutoDdl;
import lazycat.series.sqljam.MappingFault;
import lazycat.series.sqljam.generator.IdentifierGenerator;

/**
 * Table Description
 * 
 * @author Fred Feng
 * @see StandardColumnDefinition
 * @see StandardPrimaryKeyDefinition
 */
public class StardardTableDefinition implements TableDefinition {

	private final SchemaDefinition schemaDefinition;
	private final Class<?> mappedClass;
	private final String tableName;
	private AutoDdl autoDdl;
	private boolean ddlContainsConstraint;
	private String comment;

	Map<String, ColumnDefinition> columns = new LinkedHashMap<String, ColumnDefinition>();
	Map<String, PrimaryKeyDefinition> primaryKeys = new LinkedHashMap<String, PrimaryKeyDefinition>();
	Map<String, ForeignKeyDefinition> foreignKeys = new LinkedHashMap<String, ForeignKeyDefinition>();
	Map<String, UniqueKeyDefinition> uniqueKeys = new LinkedHashMap<String, UniqueKeyDefinition>();

	Map<String, IdentifierGenerator> sequences = new HashMap<String, IdentifierGenerator>();
	Map<String, IdentifierGenerator> identifiers = new HashMap<String, IdentifierGenerator>();

	StardardTableDefinition(SchemaDefinition schemaDefinition, Class<?> mappedClass, String tableName) {
		this.schemaDefinition = schemaDefinition;
		this.mappedClass = mappedClass;
		this.tableName = tableName;
	}

	public String getComment() {
		return comment;
	}

	public AutoDdl getAutoDdl() {
		return autoDdl;
	}

	public Class<?> getMappedClass() {
		return mappedClass;
	}

	public String getTableName() {
		return tableName;
	}

	public boolean isDdlContainsConstraint() {
		return ddlContainsConstraint;
	}

	public void setDdlContainsConstraint(boolean ddlContainsConstraint) {
		this.ddlContainsConstraint = ddlContainsConstraint;
	}

	public void setAutoDdl(AutoDdl autoDdl) {
		this.autoDdl = autoDdl;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCatalog() {
		return getSchemaDefinition().getCatalog();
	}

	public String getSchema() {
		return getSchemaDefinition().getSchema();
	}

	public String getFullTableName() {
		return null;
	}

	public IdentifierGenerator getSequenceGenerator(String propertyName) {
		return sequences.get(propertyName);
	}

	public IdentifierGenerator getUserDefinedGenerator(String propertyName) {
		return identifiers.get(propertyName);
	}

	public ColumnDefinition getColumn(String mappedProperty) {
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
		throw new MappingFault("Unknow mappedProperty: " + mappedProperty);
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
