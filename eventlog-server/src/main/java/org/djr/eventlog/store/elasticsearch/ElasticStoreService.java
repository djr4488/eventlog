package org.djr.eventlog.store.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.djr.eventlog.store.EventLogStore;
import org.djr.eventlog.store.elasticsearch.cdi.ElasticSearch;
import org.djr.eventlog.store.elasticsearch.cdi.ElasticSearchConfig;
import org.djr.eventlog.rest.EventLogRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
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
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by djr4488 on 11/12/17.
 */
@ApplicationScoped
@Named(value = "ELASTIC")
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
            byte[] jsonBytes = objectMapper.writeValueAsBytes(eventLogRequest);
            String jsonString = new String(jsonBytes);
            log.debug("doStoreEventLogInElasticSearch() jsonString:{}", jsonString);
            IndexRequest indexRequest =
                    new IndexRequest(URLEncoder.encode(eventLogRequest.getApplicationName(), "UTF-8"),
                            URLEncoder.encode(eventLogRequest.getEventCode(), "UTF-8"))
                            .source(jsonBytes, XContentType.JSON);
            client.index(indexRequest);
        } catch (Exception ex) {
            log.error("doStoreEventLogInElasticSearch() failed to store event in elastic search", ex);
        }
    }

    public List<EventLogRequest> searchDateRangeForApplicationAndServer(DateTime from, DateTime to, String application,
                                                                             String server) {
        List<EventLogRequest> elr = null;
        try {
            SearchRequest searchRequest =
                    new SearchRequest();
            SearchSourceBuilder builder = new SearchSourceBuilder();
            RangeQueryBuilder rqBuilder = QueryBuilders.rangeQuery("eventOccurredAt")
                    .from(from.getMillis())
                    .to(to.getMillis());
            QueryBuilder appBuilder = QueryBuilders.matchQuery("applicationName", application);
            QueryBuilder envBuilder = QueryBuilders.matchQuery("server", server);
            builder.query(rqBuilder).query(appBuilder).query(envBuilder);
            searchRequest.source(builder);
            SearchResponse searchResponse = client.search(searchRequest);
            log.debug("searchDateRange() from:{}, to:{}, searchResponse:{}", from.getMillis(), to.getMillis(), searchResponse);
        } catch (Exception ex) {
            log.error("search()", ex);
        }
        return elr;
    }
}
