package lazycat.series.sqljam;

import lazycat.series.sqljam.generator.IdentifierGenerator;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * GeneratedType
 * 
 * @author Fred Feng
 * @version 1.0
 */
public enum GeneratedType {

	SEQUENCE {

		public IdentifierGenerator getIdentifierGenerator(TableDefinition tableDefinition, String propertyName) {
			return tableDefinition.getSequenceGenerator(propertyName);
		}

	},
	USER_DEFINED {
		public IdentifierGenerator getIdentifierGenerator(TableDefinition tableDefinition, String propertyName) {
			return tableDefinition.getUserDefinedGenerator(propertyName);
		}
	};

	public abstract IdentifierGenerator getIdentifierGenerator(TableDefinition tableDefinition, String propertyName);

}
