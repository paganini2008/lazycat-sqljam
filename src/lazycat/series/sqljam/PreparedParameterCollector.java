package lazycat.series.sqljam;

import java.util.ArrayList;
import java.util.List;

import lazycat.series.jdbc.JdbcType;

/**
 * ParameterCollector for java.sql.PreparedStatement
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class PreparedParameterCollector implements ParameterCollector {

	private final List<Object> parameterList = new ArrayList<Object>();
	private final List<JdbcType> jdbcTypeList = new ArrayList<JdbcType>();
	private final List<Object[]> parameterArrayList = new ArrayList<Object[]>();

	public void addBatch() {
		batchMode = true;
		if (parameterList.size() > 0) {
			parameterArrayList.add(parameterList.toArray());
		}
		parameterList.clear();
	}

	private boolean batchMode;

	public void setParameter(Object parameter, JdbcType jdbcType) {
		parameterList.add(parameter);
		if (!batchMode) {
			jdbcTypeList.add(jdbcType);
		}
	}

	public int getBatchSize() {
		return parameterArrayList.size();
	}

	public Object[] getParameters() {
		return parameterList.toArray();
	}

	public JdbcType[] getJdbcTypes() {
		return jdbcTypeList.toArray(new JdbcType[0]);
	}

	public List<Object[]> getParameterList() {
		return parameterArrayList;
	}

}
