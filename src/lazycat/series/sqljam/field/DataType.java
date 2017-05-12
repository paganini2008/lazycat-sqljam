package lazycat.series.sqljam.field;

/**
 * SqlType
 * 
 * @author Fred Feng
 * @version 1.0
 */
public enum DataType {

	NULL {
		public Data createData(Object o) {
			return Text.NULL;
		}
	},

	TEXT {
		public Data createData(Object o) {
			return new Text(o);
		}
	},

	NUMBER {
		public Data createData(Object o) {
			return new NumberData(o);
		}
	},
	VARCHAR {
		public Data createData(Object o) {
			return new StringData(o);
		}
	},
	DATE {
		public Data createData(Object o) {
			return new DateData(o);
		}
	};

	public abstract Data createData(Object o);

}
