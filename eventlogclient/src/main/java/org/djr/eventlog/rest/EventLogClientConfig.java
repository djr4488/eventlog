package org.djr.eventlog.rest;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:EventLogClientConfig.properties"
})
public interface EventLogClientConfig {
    @Config.DefaultValue("application/json")
    String contentType();
    @Config.DefaultValue("application/json")
    String accept();
    String username();
    String password();
}
