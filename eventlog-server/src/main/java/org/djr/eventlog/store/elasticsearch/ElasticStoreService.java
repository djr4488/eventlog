package org.djr.eventlog.store.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.djr.eventlog.EventLogStore;
import org.djr.eventlog.store.elasticsearch.cdi.ElasticSearch;
import org.djr.eventlog.store.elasticsearch.cdi.ElasticSearchConfig;
import org.djr.eventlog.rest.EventLogRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by djr4488 on 11/12/17.
 */
@ApplicationScoped
@Named("Elastic")
public class ElasticStoreService implements EventLogStore {
    private static Logger log = LoggerFactory.getLogger(ElasticStoreService.class);
    @Inject
    @ElasticSearch
    @ElasticSearchConfig(hostsPropertyName = "EventLogController.elasticHosts",
            portsPropertyName = "EventLogController.elasticPorts",
            schemePropertyName = "EventLogController.elasticSchemes",
            delineatorPropertyName = "EventLogController.delineator")
    private RestHighLevelClient client;

    public void storeEventLog(EventLogRequest eventLogRequest) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(eventLogRequest);
            log.debug("doStoreEventLogInElasticSearch() jsonString:{}", jsonString);
            IndexRequest indexRequest =
                    new IndexRequest(URLEncoder.encode("eventlogs", "UTF-8"),"event")
                            .source(jsonString, XContentType.JSON);
            IndexResponse ir = client.index(indexRequest);
            log.debug("storeEventLog() indexResponse:{}", ir);
        } catch (Exception ex) {
            log.error("doStoreEventLogInElasticSearch() failed to store event in elastic search", ex);
        }
    }

    public SearchResponse search(QueryBuilder queryBuilder)
    throws IOException {
        try {
            SearchRequest searchRequest =
                    new SearchRequest();
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(queryBuilder);
            searchRequest.source(builder);
            return client.search(searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH));
        } catch (IOException ioEx) {
            log.error("search() exception occurred:", ioEx);
            throw ioEx;
        }
    }
}
