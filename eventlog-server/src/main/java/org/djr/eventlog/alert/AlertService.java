package org.djr.eventlog.alert;

import org.djr.eventlog.EventLogProperties;
import org.djr.eventlog.rest.EventLogRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Properties;

@ApplicationScoped
public class AlertService {
    private static final Logger log = LoggerFactory.getLogger(AlertService.class);
    @Inject
    @EventLogProperties
    private Properties properties;

    public void sendAlert(EventLogRequest eventLogRequest) {
        log.debug("sendAlert() eventLogRequest:{}", eventLogRequest);
    }
}
