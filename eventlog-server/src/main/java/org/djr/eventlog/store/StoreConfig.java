package org.djr.eventlog.store;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by djr4488 on 11/15/17.
 */
@Retention(RUNTIME)
@Target({FIELD})
public @interface StoreConfig {
    String storeTypePropertyName();
}
