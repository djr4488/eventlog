package org.djr.eventlog;

import org.djr.eventlog.rest.EventLogRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

@ApplicationScoped
public class EventLogController {
    private static Logger log = LoggerFactory.getLogger(EventLogController.class);
    @Inject
    @Named("Elastic")
    private EventLogStore eventLogStore;

    public void doHandleEventLogRequest(EventLogRequest eventLogRequest) {
        log.debug("doHandleEventLogRequest() eventLogRequest:{}", eventLogRequest);
        eventLogStore.storeEventLog(eventLogRequest);
    }

    public SearchResponse doSearch(String query)
    throws IOException {
        log.debug("doSearch() query:{}", query);
        return eventLogStore.search(query);
    }

    public SearchResponse doSearch(QueryBuilder query)
    throws IOException {
        log.debug("doSearch() query:{}", query);
        return eventLogStore.search(query);
    }
}
