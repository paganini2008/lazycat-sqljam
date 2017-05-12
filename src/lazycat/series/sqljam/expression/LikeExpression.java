package lazycat.series.sqljam.expression;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Session;
import lazycat.series.sqljam.Translator;
import lazycat.series.sqljam.field.Field;
import lazycat.series.sqljam.field.StandardColumn;

/**
 * LikeExpression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class LikeExpression extends LogicalExpression {

	private final Field field;
	private final String like;
	private MatchMode matchMode = MatchMode.ANY_WHERE;
	private Character escapeChar;

	/**
	 * Match mode
	 * 
	 * @author Fred Feng
	 * @version 1.0
	 */
	public static enum MatchMode {

		START {
			public String toMatchString(String pattern) {
				return pattern + '%';
			}
		},

		END {
			public String toMatchString(String pattern) {
				return '%' + pattern;
			}
		},

		ANY_WHERE {
			public String toMatchString(String pattern) {
				return '%' + pattern + '%';
			}
		};

		public abstract String toMatchString(String pattern);

	}

	public LikeExpression(String propertyName, String like, MatchMode matchMode) {
		this(new StandardColumn(propertyName), like, matchMode);
	}

	public LikeExpression(Field field, String like, MatchMode matchMode) {
		this.field = field;
		this.like = like;
		this.matchMode = matchMode;
	}

	public LikeExpression setMatchMode(MatchMode matchMode) {
		this.matchMode = matchMode;
		return this;
	}

	public LikeExpression setEscapeChar(Character escapeChar) {
		this.escapeChar = escapeChar;
		return this;
	}

	public String getText(Session session, Translator translator, Configuration configuration) {
		return configuration.getJdbcAdmin().getFeature().like(field.getText(session, translator, configuration), like, escapeChar);
	}

	public void setParameter(Session session, Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		parameterCollector.setParameter(matchMode.toMatchString(like), JdbcType.VARCHAR);
	}

}
