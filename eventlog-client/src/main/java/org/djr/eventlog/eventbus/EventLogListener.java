package org.djr.eventlog.eventbus;

import com.google.common.eventbus.Subscribe;
import org.djr.eventlog.eventbus.client.EventLogClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class EventLogListener {
    private static final Logger log = LoggerFactory.getLogger(EventLogListener.class);
    @Inject
    private EventLogClient eventLogClient;

    @Subscribe
    public void eventLogMessageListener(EventLogMessage eventLogMessage) {
        log.info("eventLogMessageListener() received eventLogMessage:{}", eventLogMessage);
        eventLogClient.doPostEventLogRequest(eventLogMessage.getEventLogRequest());
    }
}
