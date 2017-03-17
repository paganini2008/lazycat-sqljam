package lazycat.series.sqljam;

import lazycat.series.jdbc.mapper.RowMapper;

public interface Transformer<T> {

	RowMapper<T> transform();

}
