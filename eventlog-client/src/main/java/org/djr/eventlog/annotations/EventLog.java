package org.djr.eventlog.annotations;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@InterceptorBinding
@Inherited
@Documented
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface EventLog {
    boolean alertOnBusinessException() default false;
    boolean alertOnSystemException() default false;
    boolean generateTrackingIdForEntry() default false;
    String name() default "";
    @Nonbinding
    Class[] businessExceptions() default {  };
}
