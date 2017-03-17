package lazycat.series.sqljam;

import java.util.Map;

import lazycat.series.jdbc.mapper.MapRowMapper;
import lazycat.series.jdbc.mapper.ObjectArrayRowMapper;
import lazycat.series.jdbc.mapper.RowMapper;

public class Transformers {

	public static Transformer<Map<String, Object>> MAP = new Transformer<Map<String, Object>>() {
		public RowMapper<Map<String, Object>> transform() {
			return new MapRowMapper();
		}
	};

	public static Transformer<Object[]> ARRAY = new Transformer<Object[]>() {
		public RowMapper<Object[]> transform() {
			return new ObjectArrayRowMapper();
		}
	};

	private Transformers() {
	}

}
