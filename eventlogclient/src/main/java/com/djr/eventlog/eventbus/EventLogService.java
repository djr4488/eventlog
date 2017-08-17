package com.djr.eventlog.eventbus;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class EventLogService {
    private Logger log = LoggerFactory.getLogger(EventLogService.class);
    private EventBus eventBus;
    @Inject
    private EventLogListener eventLogListener;

    @PostConstruct
    public void postConstruct() {
        log.debug("postConstruct() adding event log listener");
        eventBus = new EventBus();
        eventBus.register(eventLogListener);
    }

    public void publishEventLogMessage(EventLogMessage eventLogMessage) {
        log.debug("publishEventLogMessage() publishing eventLogMessage:{}", eventLogMessage);
        eventBus.post(eventLogMessage);
    }
}
