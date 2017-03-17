package lazycat.series.sqljam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import lazycat.series.sqljam.AutoDdl;

/**
 * Table Definition
 * 
 * @author Fred Feng
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

	String catalog() default "";

	String schema() default "";

	String name() default "";

	AutoDdl autoDdl() default AutoDdl.DEFAULT;

	String comment() default "";

	boolean ddlContainsConstraint() default true;

}
