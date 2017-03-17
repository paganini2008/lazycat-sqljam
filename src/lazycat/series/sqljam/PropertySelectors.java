package lazycat.series.sqljam;

import lazycat.series.jdbc.JdbcType;

/**
 * PropertySelectors
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class PropertySelectors {

	public static final PropertySelector ALL = new AllPropertySelector();
	public static final PropertySelector NON_NULL = new NonNullPropertySelector();
	public static final PropertySelector NON_NULL_OR_ZERO = new NonNullOrZeroPropertySelector();

	private static class AllPropertySelector implements PropertySelector {

		public boolean include(Object object, String propertyName, JdbcType type) {
			return true;
		}

	}

	private static class NonNullOrZeroPropertySelector implements PropertySelector {

		public boolean include(Object object, String propertyName, JdbcType type) {
			return (object != null) || ((object instanceof Number) && ((Number) object).doubleValue() != 0);
		}

	}

	private static class NonNullPropertySelector implements PropertySelector {

		public boolean include(Object object, String propertyName, JdbcType type) {
			return object != null;
		}

	}

	private PropertySelectors() {
	}
}
