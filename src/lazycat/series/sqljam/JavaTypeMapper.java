package lazycat.series.sqljam;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import lazycat.series.jdbc.JdbcType;

/**
 * JavaTypeMapper
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class JavaTypeMapper {

	private final Map<Type, JdbcType> mapper = new HashMap<Type, JdbcType>();

	public void put(Type javaType, JdbcType jdbcType) {
		mapper.put(javaType, jdbcType);
	}

	public JdbcType get(Type javaType) {
		return mapper.get(javaType);
	}

}
