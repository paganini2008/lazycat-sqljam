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
			jdbcAdmin.updateTable(mappedClass);
		} else {
			TableDefinition tableDefinition = jdbcAdmin.getConfiguration().getMetaData().getTable(mappedClass);
			ForeignKeyDefinition[] definitions = tableDefinition.getForeignKeyDefinitions();
			if (definitions != null) {
				for (ForeignKeyDefinition fkDefinition : definitions) {
					jdbcAdmin.resolve(fkDefinition.getRefMappedClass());
				}
			}
			jdbcAdmin.createTable(mappedClass);
		}
	}
}
