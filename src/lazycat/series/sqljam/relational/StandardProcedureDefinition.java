package lazycat.series.sqljam.relational;

import java.util.HashMap;
import java.util.Map;

import lazycat.series.jdbc.JdbcType;

public class StandardProcedureDefinition {

	private final SchemaDefinition schemaDefinition;
	private final String name;
	private final Map<Integer, JdbcType> output = new HashMap<Integer, JdbcType>();

	public StandardProcedureDefinition(SchemaDefinition schemaDefinition, String name) {
		this.schemaDefinition = schemaDefinition;
		this.name = name;
	}

}
