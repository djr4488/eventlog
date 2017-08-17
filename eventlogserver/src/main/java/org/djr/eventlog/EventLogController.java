package org.djr.eventlog;

import org.djr.eventlog.store.EventLogStorageService;
import org.djr.eventlog.rest.EventLogRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class EventLogController {
    private static Logger log = LoggerFactory.getLogger(EventLogController.class);
    @Inject
    private EventLogStorageService eventLogStorageService;

    public void doHandleEventLogRequest(EventLogRequest eventLogRequest) {
        log.debug("doHandleEventLogRequest() eventLogRequest:{}", eventLogRequest);
        eventLogStorageService.storeEventLog(eventLogRequest);
    }
}