package lazycat.series.sqljam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generator
 * 
 * @author Fred Feng
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Generator {

	String value();

	String name() default "global";

}
