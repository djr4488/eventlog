package org.djr.eventlog;


import org.djr.eventlog.rest.EventLogRequest;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;

/**
 * Created by djr4488 on 11/15/17.
 */
public interface EventLogStore {
    void storeEventLog(EventLogRequest eventLogRequest);
    SearchResponse search(String searchQuery) throws IOException;
}