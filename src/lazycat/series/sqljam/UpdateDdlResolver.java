package lazycat.series.sqljam;

import lazycat.series.sqljam.relational.ForeignKeyDefinition;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * UpdateDdlResolver
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class UpdateDdlResolver implements DdlResolver {

	public void resolve(JdbcAdmin jdbcAdmin, Class<?> mappedClass) {
		if (jdbcAdmin.tableExists(mappedClass)) {
			jdbcAdmin.alterTable(mappedClass);
		} else {
			TableDefinition tableDefinition = jdbcAdmin.getConfiguration().getTableDefinition(mappedClass);
			ForeignKeyDefinition[] fkDefinitions = tableDefinition.getForeignKeyDefinitions();
			if (fkDefinitions != null) {
				for (ForeignKeyDefinition fkDefinition : fkDefinitions) {

					jdbcAdmin.resolve(fkDefinition.getRefMappedClass());
				}
			}
			jdbcAdmin.createTable(mappedClass);
		}
	}
}
