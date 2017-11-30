package org.djr.eventlog;

import org.djr.eventlog.rest.EventLogRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.avg.ParsedAvg;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class EventLogController {
    private static Logger log = LoggerFactory.getLogger(EventLogController.class);
    @Inject
    @Named("Elastic")
    private EventLogStore eventLogStore;
    @Inject
    private AggregationService aggregationService;

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

    public Map<String, SearchResponse> doAggregationAvgAndCountSearch(String applicationName, String eventCode)
    throws IOException {
        log.debug("doAggregationSearch() applicationName:{}, eventCode:{}", applicationName,
                eventCode);
        AggregationBuilder abAvg = AggregationBuilders.avg("apps_and_event_codes_avg")
                .field("executeTime");
        AggregationBuilder abCount = AggregationBuilders.count("apps_and_event_codes_count")
                .field("executeTime");
        Map<String, SearchResponse> results = new HashMap<>();
        aggregationService.getTodayResultsForApplicationNameAndEventCode(applicationName, eventCode, results, abAvg, abCount);
        aggregationService.getYesterdayResultsForApplicationNameAndEventCode(applicationName, eventCode, results, abAvg, abCount);
        aggregationService.getLastWeekSameDayResultsForApplicationNameAndEventCode(applicationName, eventCode, results, abAvg, abCount);
        aggregationService.getLast7DaysResultsForApplicationNameAndEventCode(applicationName, eventCode, results, abAvg, abCount);
        aggregationService.getLast30DaysResultsForApplicationNameAndEventCode(applicationName, eventCode, results, abAvg, abCount);
        return results;
    }
}
