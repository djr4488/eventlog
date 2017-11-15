package org.djr.eventlog;

import org.djr.eventlog.rest.EventLogRequest;
import org.djr.eventlog.store.EventLogStore;
import org.djr.eventlog.store.EventLogStoreProducer;
import org.djr.eventlog.store.StoreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class EventLogController {
    private static Logger log = LoggerFactory.getLogger(EventLogController.class);
    @Inject
    @StoreConfig(storeTypePropertyName = "EventLogController.eventStoreType")
    @EventLogStoreProducer
    private EventLogStore eventLogStore;

    public void doHandleEventLogRequest(EventLogRequest eventLogRequest) {
        log.debug("doHandleEventLogRequest() eventLogRequest:{}", eventLogRequest);
        eventLogStore.storeEventLog(eventLogRequest);
    }
}
