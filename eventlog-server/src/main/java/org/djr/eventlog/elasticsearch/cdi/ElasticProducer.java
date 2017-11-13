package org.djr.eventlog.elasticsearch.cdi;

import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.util.Properties;

/**
 * Created by djr4488 on 11/12/17.
 */
@ElasticSearch
public class ElasticProducer {
    private static Logger log = LoggerFactory.getLogger(ElasticProducer.class);
    @Inject
    @ElasticProperties
    private Properties properties;

    @Produces
    @ElasticSearch
    public RestHighLevelClient produceElasticSearchHighLevelRestClient(InjectionPoint injectionPoint) {
        return null;
    }
}
