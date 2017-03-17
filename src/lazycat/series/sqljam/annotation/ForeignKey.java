package lazycat.series.sqljam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import lazycat.series.sqljam.CascadeAction;

/**
 * Foreign Key Definition
 * 
 * @author Fred Feng
 * @see PrimaryKey
 * @see UniqueKey
 * @see Column
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ForeignKey {

	String name() default "";

	String refMappedProperty();

	Class<?> refMappedClass();

	int position() default 0;

	boolean required() default true;

	CascadeAction onDelete() default CascadeAction.NO_ACTION;

	CascadeAction onUpdate() default CascadeAction.NO_ACTION;

}
