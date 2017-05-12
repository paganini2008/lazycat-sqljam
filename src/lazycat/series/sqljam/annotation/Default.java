package lazycat.series.sqljam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import lazycat.series.sqljam.field.DataType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Default {

	String value();

	DataType dataType() default DataType.TEXT;

}
