package org.djr.eventlog.store.elasticsearch.cdi;

import org.apache.http.HttpHost;
import org.djr.eventlog.EventLogProperties;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by djr4488 on 11/12/17.
 */
@ApplicationScoped
@ElasticSearch
public class ElasticProducer {
    private static Logger log = LoggerFactory.getLogger(ElasticProducer.class);
    @Inject
    @EventLogProperties(name = "elastic.properties")
    private Properties properties;

    @Produces
    @ElasticSearch
    public RestHighLevelClient produceElasticSearchHighLevelRestClient(InjectionPoint injectionPoint)
    throws IOException, NullPointerException {
        log.debug("produceElasticSearchHighLevelRestClient() started");
        ElasticSearchConfig config = injectionPoint.getAnnotated().getAnnotation(ElasticSearchConfig.class);
        RestHighLevelClient client = null;
        if (null != config) {
            String delineator = properties.getProperty(config.delineatorPropertyName());
            String[] hosts = getStringArray(properties.getProperty(config.hostsPropertyName()), delineator);
            int[] ports = getIntArray(properties.getProperty(config.portsPropertyName()), delineator);
            String[] schemes = getStringArray(properties.getProperty(config.schemePropertyName()), delineator);
            HttpHost[] httpHosts = new HttpHost[hosts.length];
            for (int i = 0; i < hosts.length; i++) {
                httpHosts[i] = new HttpHost(hosts[i], ports[i], schemes[i]);
            }
            client = new RestHighLevelClient(
                    RestClient.builder(httpHosts)
            );
        }
        return client;
    }

    private String[] getStringArray(String toConvert, String delineator) {
        String[] splits = null;
        if (null != toConvert) {
            splits = toConvert.split(delineator);
        }
        return splits;
    }

    private int[] getIntArray(String toConvert, String delineator) {
        String[] splits = null;
        int[] asInt = null;
        if (null != toConvert) {
            splits = toConvert.split(delineator);
            asInt = new int[splits.length];
            int i = 0;
            for (String split : splits) {
                asInt[i] = Integer.parseInt(split);
                i++;
            }
        }
        return asInt;
    }
}
