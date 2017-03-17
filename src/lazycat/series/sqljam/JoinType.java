package lazycat.series.sqljam;

import lazycat.series.sqljam.feature.Feature;

/**
 * JoinType
 * 
 * @author Fred Feng
 * @version 1.0
 */
public enum JoinType {

	INNER_JOIN {
		public String sqlText(Feature feature, String str) {
			return feature.innerJoin(str);
		}

	},
	LEFT_JOIN {
		public String sqlText(Feature feature, String str) {
			return feature.leftJoin(str);
		}
	},
	RIGHT_JOIN {
		public String sqlText(Feature feature, String str) {
			return feature.rightJoin(str);
		}
	},
	FULL_JOIN {
		public String sqlText(Feature feature, String str) {
			return feature.fullJoin(str);
		}
	},
	CROSS_JOIN {
		public String sqlText(Feature feature, String str) {
			return feature.crossJoin(str);
		}
	};

	public abstract String sqlText(Feature feature, String str);

}
