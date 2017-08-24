package org.djr.eventlog.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({PARAMETER})
public @interface EventLogParameter {
    boolean scanForEventLogAttributes() default false;
    String eventLogParameterName() default "";
}
