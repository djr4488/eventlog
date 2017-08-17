package com.djr.eventlog.eventbus;

import com.djr.eventlog.EventLogController;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class EventLogListener {
    private static final Logger log = LoggerFactory.getLogger(EventLogListener.class);
    @Inject
    private EventLogController eventLogController;

    @Subscribe
    public void eventLogMessageListener(EventLogMessage eventLogMessage) {
        log.info("eventLogMessageListener() received eventLogMessage:{}", eventLogMessage);
        eventLogController.doHandleEventLogMessage(eventLogMessage);
    }
}
