package org.djr.eventlog.store;

import org.djr.eventlog.rest.EventLogRequest;

/**
 * Created by djr4488 on 11/15/17.
 */
public interface EventLogStore {
    void storeEventLog(EventLogRequest eventLogRequest);
}
