package org.djr.eventlog.store.elasticsearch.cdi;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by djr4488 on 11/12/17.
 */
@Retention(RUNTIME)
@Target({FIELD})
public @interface ElasticSearchConfig {
    String hostsPropertyName();
    String portsPropertyName();
    String schemePropertyName();
    String delineatorPropertyName();
}
