package org.djr.eventlog.reporter;

import org.aeonbits.owner.Config;

/**
 * Created by djr4488 on 8/19/17.
 */
@Config.Sources({
        "classpath:EventLogReporterBeanConfig.properties"
})
public interface EventLogReporterBeanConfig extends Config {
    String applicationName();
    String trackingIdentifier();
    String environment();
}
