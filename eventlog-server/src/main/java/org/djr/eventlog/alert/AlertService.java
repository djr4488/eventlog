package org.djr.eventlog.alert;

import org.djr.eventlog.rest.EventLogRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AlertService {
    private static final Logger log = LoggerFactory.getLogger(AlertService.class);
    @Inject
    private String alertEmail;

    public void sendAlert(EventLogRequest eventLogRequest) {
        log.debug("sendAlert() eventLogRequest:{}", eventLogRequest);
    }
}
