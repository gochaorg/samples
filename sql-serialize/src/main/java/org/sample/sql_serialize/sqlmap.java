package org.sample.sql_serialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface sqlmap {
    String value() default "";
    boolean ignore() default false;
    String[] ignoreFields() default {};
    boolean trim() default false;
}
