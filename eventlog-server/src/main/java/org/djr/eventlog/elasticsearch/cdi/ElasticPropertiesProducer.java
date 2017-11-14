package org.djr.eventlog.elasticsearch.cdi;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@ApplicationScoped
public class ElasticPropertiesProducer {
    @Produces
    @ElasticProperties
    public Properties loadRetrofitConfigProperties()
    throws IOException {
        Properties prop = new Properties();
        InputStream in = getClass().getClassLoader().getResourceAsStream("elastic.properties");
        prop.load(in);
        in.close();
        return prop;
    }
}
