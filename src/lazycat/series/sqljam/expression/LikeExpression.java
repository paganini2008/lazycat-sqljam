package lazycat.series.sqljam.expression;

import lazycat.series.jdbc.JdbcType;
import lazycat.series.sqljam.Configuration;
import lazycat.series.sqljam.ParameterCollector;
import lazycat.series.sqljam.Translator;

/**
 * Like expression
 * 
 * @author Fred Feng
 * @version 1.0
 */
public class LikeExpression implements Expression {

	private final Expression expression;
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
		this(new Column(propertyName), like, matchMode);
	}

	public LikeExpression(Expression expression, String like, MatchMode matchMode) {
		this.expression = expression;
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

	public String getText(Translator translator, Configuration configuration) {
		return configuration.getFeature().like(expression.getText(translator, configuration), like, escapeChar);
	}

	public void setParameter(Translator translator, ParameterCollector parameterCollector, Configuration configuration) {
		parameterCollector.setParameter(matchMode.toMatchString(like), JdbcType.VARCHAR);
	}

}
