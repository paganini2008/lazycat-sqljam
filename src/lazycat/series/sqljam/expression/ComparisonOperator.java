package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.feature.Feature;

/**
 * Operator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public enum ComparisonOperator {

	EQ {
		public String getText(Feature feature, String left, String right) {
			return feature.eq(left, right);
		}

	},
	NE {
		public String getText(Feature feature, String left, String right) {
			return feature.ne(left, right);
		}
	},
	NE_ALL {
		public String getText(Feature feature, String left, String right) {
			return feature.neAll(left, right);
		}
	},
	NE_ANY {
		public String getText(Feature feature, String left, String right) {
			return feature.neAny(left, right);
		}
	},
	LT {
		public String getText(Feature feature, String left, String right) {
			return feature.lt(left, right);
		}
	},
	LT_ALL {
		public String getText(Feature feature, String left, String right) {
			return feature.ltAll(left, right);
		}
	},
	LT_ANY {
		public String getText(Feature feature, String left, String right) {
			return feature.ltAny(left, right);
		}
	},
	GT {
		public String getText(Feature feature, String left, String right) {
			return feature.gt(left, right);
		}
	},
	GT_ALL {
		public String getText(Feature feature, String left, String right) {
			return feature.gtAll(left, right);
		}
	},
	GT_ANY {
		public String getText(Feature feature, String left, String right) {
			return feature.gtAny(left, right);
		}
	},
	LTE {
		public String getText(Feature feature, String left, String right) {
			return feature.lte(left, right);
		}
	},
	LTE_ALL {
		public String getText(Feature feature, String left, String right) {
			return feature.lteAll(left, right);
		}
	},
	LTE_ANY {
		public String getText(Feature feature, String left, String right) {
			return feature.lteAny(left, right);
		}
	},
	GTE {
		public String getText(Feature feature, String left, String right) {
			return feature.gte(left, right);
		}
	},
	GTE_ALL {
		public String getText(Feature feature, String left, String right) {
			return feature.gteAll(left, right);
		}
	},
	GTE_ANY {
		public String getText(Feature feature, String left, String right) {
			return feature.lteAny(left, right);
		}
	};

	public abstract String getText(Feature feature, String left, String right);

}
