package org.djr.eventlog.eventbus;

import org.djr.eventlog.EventLogClientController;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class EventLogListener {
    private static final Logger log = LoggerFactory.getLogger(EventLogListener.class);
    @Inject
    private EventLogClientController eventLogClientController;

    @Subscribe
    public void eventLogMessageListener(EventLogMessage eventLogMessage) {
        log.info("eventLogMessageListener() received eventLogMessage:{}", eventLogMessage);
        eventLogClientController.doHandleEventLogMessage(eventLogMessage);
    }
}
