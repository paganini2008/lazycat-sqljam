package lazycat.series.sqljam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Primary Key Definition
 * @author Fred Feng
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface PrimaryKey {

	String name() default "";

	int position() default 0;

}
