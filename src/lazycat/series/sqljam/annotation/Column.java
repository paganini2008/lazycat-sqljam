package lazycat.series.sqljam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import lazycat.series.jdbc.JdbcType;

/**
 * Column Definition
 * 
 * @author Fred Feng
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Column {

	boolean nullable() default true;

	boolean autoIncrement() default false;

	boolean unsigned() default false;

	boolean store() default true;

	String name() default "";

	long length() default -1;

	int precision() default -1;

	int scale() default -1;

	JdbcType jdbcType() default JdbcType.OTHER;

	String comment() default "";

	String defaultValue() default "";

}
