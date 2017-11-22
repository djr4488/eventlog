package org.djr.eventlog;

import org.djr.eventlog.rest.EventLogRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.joda.time.DateTime;
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

    public SearchResponse doSearchByTrackingId(String trackingId)
    throws IOException {
        log.debug("doSearch() trackingId:{}", trackingId);
        QueryBuilder qb = QueryBuilders.matchQuery("tracking_identifier", trackingId);
        return eventLogStore.search(qb);
    }

    public SearchResponse doSearchByTodayApplicationNameAndEventCode(String applicationName, String eventCode)
    throws IOException {
        log.debug("doSearch() applicationName:{}, eventCode:{}", applicationName, eventCode);
        long millisAtStartOfDay = DateTime.now().withTimeAtStartOfDay().getMillis();
        long millisAtStartOfDayTomorrow = DateTime.now().withTimeAtStartOfDay().plusDays(1).getMillis();
        QueryBuilder qb = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("eventOccurredAt").from(millisAtStartOfDay).to(millisAtStartOfDayTomorrow))
                .must(QueryBuilders.matchPhraseQuery("applicationName", applicationName))
                .must(QueryBuilders.matchPhraseQuery("eventCode", eventCode));
        return eventLogStore.search(qb);
    }
}
