package lazycat.series.sqljam;

import lazycat.series.sqljam.generator.Generator;
import lazycat.series.sqljam.relational.TableDefinition;

/**
 * GeneratedType
 * 
 * @author Fred Feng
 * @version 1.0
 */
public enum GeneratedType {

	SEQUENCE {

		public Generator getIdentifierGenerator(TableDefinition tableDefinition, String propertyName) {
			return tableDefinition.getSequenceGenerator(propertyName);
		}

	},
	USER_DEFINED {
		public Generator getIdentifierGenerator(TableDefinition tableDefinition, String propertyName) {
			return tableDefinition.getUserDefinedGenerator(propertyName);
		}
	};

	public abstract Generator getIdentifierGenerator(TableDefinition tableDefinition, String propertyName);

}
