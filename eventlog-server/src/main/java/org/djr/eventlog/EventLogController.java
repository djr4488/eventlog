package org.djr.eventlog;

import org.djr.eventlog.alert.AlertService;
import org.djr.eventlog.rest.EventLogRequest;
import org.djr.eventlog.store.AggregationService;
import org.djr.eventlog.store.EventLogStore;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentilesMethod;
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
    @Inject
    private AlertService alertService;

    public void doHandleEventLogRequest(EventLogRequest eventLogRequest) {
        log.debug("doHandleEventLogRequest() eventLogRequest:{}", eventLogRequest);
        eventLogStore.storeEventLog(eventLogRequest);
        if ((eventLogRequest.getAlertOnBusinessException() || eventLogRequest.getAlertOnSystemException())
                && null != eventLogRequest.getErrorCode()) {
            alertService.sendAlert(eventLogRequest);
        }
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
        AggregationBuilder abStats = AggregationBuilders.extendedStats("stats")
                .field("executeTime");
        AggregationBuilder abPercentiles = AggregationBuilders.percentiles("percentiles")
                .field("executeTime")
                .keyed(false);
        AggregationBuilder abHistogram = AggregationBuilders.histogram("histogram")
                .field("executeTime")
                .interval(100)
                .minDocCount(1);
        Map<String, SearchResponse> results = new HashMap<>();
        aggregationService.getTodayResultsForApplicationNameAndEventCode(applicationName, eventCode, results,
                abStats, abPercentiles);
        aggregationService.getYesterdayResultsForApplicationNameAndEventCode(applicationName, eventCode, results,
                abStats, abPercentiles);
        aggregationService.getLastWeekSameDayResultsForApplicationNameAndEventCode(applicationName, eventCode, results,
                abStats, abPercentiles);
        aggregationService.getLast7DaysResultsForApplicationNameAndEventCode(applicationName, eventCode, results,
                abStats, abPercentiles);
        aggregationService.getLast30DaysResultsForApplicationNameAndEventCode(applicationName, eventCode, results,
                abStats, abPercentiles);
        aggregationService.getLastMonthResultsForApplicationNameAndEventCode(applicationName, eventCode, results,
                abStats, abPercentiles);
        aggregationService.getThisMonthResultsForApplicationNameAndEventCode(applicationName, eventCode, results,
                abStats, abPercentiles);
        return results;
    }
}
