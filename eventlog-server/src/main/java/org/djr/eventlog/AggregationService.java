package org.djr.eventlog;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.ParsedStats;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Map;

@ApplicationScoped
public class AggregationService {
    private static Logger log = LoggerFactory.getLogger(EventLogController.class);
    @Inject
    @Named("Elastic")
    private EventLogStore eventLogStore;

    private QueryBuilder eventCodeAndApplicationNameQueryBuilder(String applicationName, String eventCode, long startMillis,
                                                                 long endMillis) {
        return QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("eventOccurredAt").from(startMillis).to(endMillis))
                .must(QueryBuilders.matchPhraseQuery("applicationName", applicationName))
                .must(QueryBuilders.matchPhraseQuery("eventCode", eventCode));
    }

    private void executeAggregationSearch(Map<String, SearchResponse> results, QueryBuilder queryBuilder, String aggregationRange,
                                          AggregationBuilder... abs)
    throws IOException {
        SearchResponse sr =
                eventLogStore.search(queryBuilder, abs);
        if (((ParsedStats) sr.getAggregations().get("stats")).getAvg() != 1.0D / 0.0) {
            results.put(aggregationRange, sr);
        }
    }

    public void getTodayResultsForApplicationNameAndEventCode(String applicationName, String eventCode, Map<String, SearchResponse> results,
                                                               AggregationBuilder... abs)
    throws IOException {
        long startMillis = DateTime.now().withTimeAtStartOfDay().getMillis();
        long endMillis = DateTime.now().withTimeAtStartOfDay().plusDays(1).getMillis();
        executeAggregationSearch(results, eventCodeAndApplicationNameQueryBuilder(applicationName, eventCode, startMillis, endMillis),
                "Today", abs);
    }

    public void getYesterdayResultsForApplicationNameAndEventCode(String applicationName, String eventCode, Map<String, SearchResponse> results,
                                                                   AggregationBuilder... abs)
    throws IOException {
        long startMillis = DateTime.now().withTimeAtStartOfDay().minusDays(1).getMillis();
        long endMillis = DateTime.now().withTimeAtStartOfDay().getMillis();
        executeAggregationSearch(results, eventCodeAndApplicationNameQueryBuilder(applicationName, eventCode, startMillis, endMillis),
                "Yesterday", abs);
    }

    public void getLastWeekSameDayResultsForApplicationNameAndEventCode(String applicationName, String eventCode, Map<String, SearchResponse> results,
                                                                         AggregationBuilder... abs)
    throws IOException {
        long startMillis = DateTime.now().withTimeAtStartOfDay().minusWeeks(1).getMillis();
        long endMillis = DateTime.now().withTimeAtStartOfDay().minusDays(6).getMillis();
        executeAggregationSearch(results, eventCodeAndApplicationNameQueryBuilder(applicationName, eventCode, startMillis, endMillis),
                "LastWeekSameDay", abs);
    }

    public void getLast7DaysResultsForApplicationNameAndEventCode(String applicationName, String eventCode, Map<String, SearchResponse> results,
                                                                   AggregationBuilder... abs)
    throws IOException {
        long startMillis = DateTime.now().withTimeAtStartOfDay().minusDays(7).getMillis();
        long endMillis = DateTime.now().withTimeAtStartOfDay().getMillis();
        executeAggregationSearch(results, eventCodeAndApplicationNameQueryBuilder(applicationName, eventCode, startMillis, endMillis),
                "Last7Days", abs);
    }

    public void getLast30DaysResultsForApplicationNameAndEventCode(String applicationName, String eventCode, Map<String, SearchResponse> results,
                                                                    AggregationBuilder... abs)
    throws IOException {
        long startMillis = DateTime.now().withTimeAtStartOfDay().minusDays(30).getMillis();
        long endMillis = DateTime.now().withTimeAtStartOfDay().getMillis();
        executeAggregationSearch(results, eventCodeAndApplicationNameQueryBuilder(applicationName, eventCode, startMillis, endMillis),
                "Last30Days", abs);
    }

    public void getLastMonthResultsForApplicationNameAndEventCode(String applicationName, String eventCode, Map<String, SearchResponse> results,
                                                                  AggregationBuilder... abs)
    throws IOException {
        long startMillis = DateTime.now().withTimeAtStartOfDay().withDayOfMonth(1).minusMonths(1).getMillis();
        long endMillis = DateTime.now().withTimeAtStartOfDay().withDayOfMonth(1).minusDays(1).getMillis();
        executeAggregationSearch(results, eventCodeAndApplicationNameQueryBuilder(applicationName, eventCode, startMillis, endMillis),
                "LastMonth", abs);
    }

    public void getThisMonthResultsForApplicationNameAndEventCode(String applicationName, String eventCode, Map<String, SearchResponse> results,
                                                                  AggregationBuilder... abs)
    throws IOException {
        long startMillis = DateTime.now().withTimeAtStartOfDay().withDayOfMonth(1).minusMonths(1).getMillis();
        long endMillis = DateTime.now().withTimeAtStartOfDay().getMillis();
        executeAggregationSearch(results, eventCodeAndApplicationNameQueryBuilder(applicationName, eventCode, startMillis, endMillis),
                "ThisMonth", abs);
    }
}
