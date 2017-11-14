package org.djr.eventlog.elasticsearch.cdi;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexAction;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by djr4488 on 11/12/17.
 */
@ElasticSearch
public class ElasticProducer {
    private static Logger log = LoggerFactory.getLogger(ElasticProducer.class);
    @Inject
    @ElasticProperties(propertiesFile = "elastic.properties")
    private Properties properties;

    @Produces
    @ElasticSearch
    public RestHighLevelClient produceElasticSearchHighLevelRestClient(InjectionPoint injectionPoint)
    throws IOException, NullPointerException {
        ElasticSearchConfig config = injectionPoint.getAnnotated().getAnnotation(ElasticSearchConfig.class);
        RestHighLevelClient client = null;
        if (null != config) {
            HttpHost[] httpHosts = new HttpHost[config.hostConfigs().length];
            int i = 0;
            for (HostConfig hostConfig : config.hostConfigs()) {
                httpHosts[i] = new HttpHost(properties.getProperty(hostConfig.host()),
                        Integer.parseInt(properties.getProperty(hostConfig.port())),
                        properties.getProperty(hostConfig.protocol()));
                i++;
            }
            client = new RestHighLevelClient(
                    RestClient.builder(httpHosts)
                            .build()
            );
        }
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        xContentBuilder.startObject();
        xContentBuilder.field("name", "value");
        xContentBuilder.array("datapoint", "values", "values", "values");
        xContentBuilder.endObject();
        IndexRequest indexRequest = new IndexRequest("tracking_identifer", "method intercept").source(xContentBuilder);
        client.index(indexRequest);
        return client;
    }
}
