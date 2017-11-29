package org.djr.eventlog;

import org.djr.eventlog.rest.EventLogRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
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
        AggregationBuilder ab = AggregationBuilders.avg("apps_and_event_codes_avg")
                        .field("executeTime");
        Map<String, SearchResponse> results = new HashMap<>();
        results.put("Today", eventLogStore.search(getTodayResultsForApplicationNameAndEventCode(applicationName, eventCode), ab));
        results.put("Yesterday", eventLogStore.search(getYesterdayResultsForApplicationNameAndEventCode(applicationName, eventCode), ab));
        results.put("LastWeekSameDay", eventLogStore.search(getLastWeekSameDayResultsForApplicationNameAndEventCode(applicationName, eventCode), ab));
        results.put("Last7Days", eventLogStore.search(getLast7DaysResultsForApplicationNameAndEventCode(applicationName, eventCode), ab));
        results.put("Last30Days", eventLogStore.search(getLast30DaysResultsForApplicationNameAndEventCode(applicationName, eventCode), ab));
        return results;
    }

    private QueryBuilder eventCodeAndApplicationNameQueryBuilder(String applicationName, String eventCode, long startMillis,
                                                                 long endMillis) {
        return QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("eventOccurredAt").from(startMillis).to(endMillis))
                .must(QueryBuilders.matchPhraseQuery("applicationName", applicationName))
                .must(QueryBuilders.matchPhraseQuery("eventCode", eventCode));
    }

    private QueryBuilder getTodayResultsForApplicationNameAndEventCode(String applicationName, String eventCode) {
        long startMillis = DateTime.now().withTimeAtStartOfDay().getMillis();
        long endMillis = DateTime.now().withTimeAtStartOfDay().plusDays(1).getMillis();
        return eventCodeAndApplicationNameQueryBuilder(applicationName, eventCode, startMillis, endMillis);
    }

    private QueryBuilder getYesterdayResultsForApplicationNameAndEventCode(String applicationName, String eventCode) {
        long startMillis = DateTime.now().withTimeAtStartOfDay().minusDays(1).getMillis();
        long endMillis = DateTime.now().withTimeAtStartOfDay().getMillis();
        return eventCodeAndApplicationNameQueryBuilder(applicationName, eventCode, startMillis, endMillis);
    }

    private QueryBuilder getLastWeekSameDayResultsForApplicationNameAndEventCode(String applicationName, String eventCode) {
        long startMillis = DateTime.now().withTimeAtStartOfDay().minusWeeks(1).getMillis();
        long endMillis = DateTime.now().withTimeAtStartOfDay().minusDays(6).getMillis();
        return eventCodeAndApplicationNameQueryBuilder(applicationName, eventCode, startMillis, endMillis);
    }

    private QueryBuilder getLast7DaysResultsForApplicationNameAndEventCode(String applicationName, String eventCode) {
        long startMillis = DateTime.now().withTimeAtStartOfDay().minusDays(7).getMillis();
        long endMillis = DateTime.now().withTimeAtStartOfDay().getMillis();
        return eventCodeAndApplicationNameQueryBuilder(applicationName, eventCode, startMillis, endMillis);
    }

    private QueryBuilder getLast30DaysResultsForApplicationNameAndEventCode(String applicationName, String eventCode) {
        long startMillis = DateTime.now().withTimeAtStartOfDay().minusDays(30).getMillis();
        long endMillis = DateTime.now().withTimeAtStartOfDay().getMillis();
        return eventCodeAndApplicationNameQueryBuilder(applicationName, eventCode, startMillis, endMillis);
    }
}
