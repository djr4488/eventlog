package org.djr.eventlog.elasticsearch.cdi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface HostConfig {
	String host();
	String port();
	String protocol();
}
