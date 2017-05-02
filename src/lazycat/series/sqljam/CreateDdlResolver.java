package lazycat.series.sqljam;

import lazycat.series.sqljam.relational.ForeignKeyDefinition;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * CreateDdlResolver
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class CreateDdlResolver implements DdlResolver {

	public void resolve(JdbcAdmin jdbcAdmin, Class<?> mappedClass) {
		TableDefinition tableDefinition = jdbcAdmin.getConfiguration().getTableDefinition(mappedClass);
		ForeignKeyDefinition[] definitions = tableDefinition.getForeignKeyDefinitions();
		if (definitions != null) {
			for (ForeignKeyDefinition fkDefinition : definitions) {
				jdbcAdmin.resolve(fkDefinition.getRefMappedClass());
			}
		}
		if (!jdbcAdmin.tableExists(mappedClass)) {
			jdbcAdmin.createTable(mappedClass);
		}

	}
}
