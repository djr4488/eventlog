package org.djr.eventlog.store;


import org.djr.eventlog.rest.EventLogRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;

import java.io.IOException;

/**
 * Created by djr4488 on 11/15/17.
 */
public interface EventLogStore {
    void storeEventLog(EventLogRequest eventLogRequest);
    SearchResponse search(QueryBuilder queryBuilder) throws IOException;
    SearchResponse search(QueryBuilder queryBuilder, AggregationBuilder... aggregationBuilder) throws IOException;
}
