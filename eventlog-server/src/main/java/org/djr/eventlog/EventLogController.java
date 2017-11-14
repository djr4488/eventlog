package org.djr.eventlog;

import org.djr.eventlog.elasticsearch.cdi.ElasticSearch;
import org.djr.eventlog.elasticsearch.cdi.ElasticSearchConfig;
import org.djr.eventlog.store.EventLogStorageService;
import org.djr.eventlog.rest.EventLogRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class EventLogController {
    private static Logger log = LoggerFactory.getLogger(EventLogController.class);
    @Inject
    private EventLogStorageService eventLogStorageService;
    @Inject
    @ElasticSearch
    @ElasticSearchConfig(hostsPropertyName = "EventLogController.elasticHosts",
        portsPropertyName = "EventLogController.elasticPorts",
        schemePropertyName = "EventLogController.elasticSchemes")
    private RestHighLevelClient client;

    public void doHandleEventLogRequest(EventLogRequest eventLogRequest) {
        log.debug("doHandleEventLogRequest() eventLogRequest:{}", eventLogRequest);
        eventLogStorageService.storeEventLog(eventLogRequest);
    }

    public void doStoreEventLogInElasticSearch(EventLogRequest eventLogRequest)
    throws IOException {

        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        xContentBuilder.startObject();
        xContentBuilder.field("name", "value");
        xContentBuilder.array("datapoint", "values", "values", "values");
        xContentBuilder.endObject();
        IndexRequest indexRequest = new IndexRequest("tracking_identifer", "method intercept").source(xContentBuilder);
        client.index(indexRequest);
    }
}
