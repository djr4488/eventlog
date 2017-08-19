package org.djr.eventlog.rest;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:EventLogTransportConfig.properties"
})
public interface EventLogTransportConfig extends Config {
    @DefaultValue("localhost")
    String baseUrl();
    @DefaultValue("true")
    Boolean enableTrafficLogging();
}