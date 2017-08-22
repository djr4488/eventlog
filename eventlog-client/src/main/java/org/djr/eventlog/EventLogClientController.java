package org.djr.eventlog;

import org.djr.eventlog.eventbus.EventLogMessage;
import org.djr.eventlog.rest.EventLogClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class EventLogClientController {
    private Logger log = LoggerFactory.getLogger(EventLogClientController.class);
    @Inject
    private EventLogClient eventLogClient;

    public void doHandleEventLogMessage(EventLogMessage eventLogMessage) {
        log.debug("doHandleEventLogMessage() entered eventLogMessage:{}", eventLogMessage);
        eventLogClient.doPostEventLogRequest(eventLogMessage.getEventLogRequest());
    }
}
