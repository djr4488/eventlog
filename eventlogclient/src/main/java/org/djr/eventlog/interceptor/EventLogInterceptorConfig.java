package org.djr.eventlog.interceptor;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:EventLogInterceptorConfig.properties"
})
public interface EventLogInterceptorConfig extends Config {
    String trackingIdentifierKey();
    String applicationNameKey();
    String environmentKey();
    String serverKey();
}
