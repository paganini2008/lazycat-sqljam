package lazycat.series.sqljam.expression;

import lazycat.series.sqljam.feature.Feature;

/**
 * ArithmeticalOperator
 * 
 * @author Fred Feng
 * @version 1.0
 */
public enum ArithmeticalOperator {
	ADD {
		public String getText(Feature feature, String left, String right) {
			return feature.plus(left, right);
		}
	},

	SUBTRACT {
		public String getText(Feature feature, String left, String right) {
			return feature.subtract(left, right);
		}
	},

	MULTIPLY {
		public String getText(Feature feature, String left, String right) {
			return feature.multiply(left, right);
		}
	},

	MODULO {
		public String getText(Feature feature, String left, String right) {
			return feature.modulo(left, right);
		}
	},

	DIVIDE {
		public String getText(Feature feature, String left, String right) {
			return feature.divide(left, right);
		}
	},
	BIT_AND {
		public String getText(Feature feature, String left, String right) {
			return feature.bitAnd(left, right);
		}
	},

	BIT_OR {
		public String getText(Feature feature, String left, String right) {
			return feature.bitOr(left, right);
		}
	},

	BIT_XOR {
		public String getText(Feature feature, String left, String right) {
			return feature.bitXor(left, right);
		}
	};

	public abstract String getText(Feature feature, String left, String right);
}
