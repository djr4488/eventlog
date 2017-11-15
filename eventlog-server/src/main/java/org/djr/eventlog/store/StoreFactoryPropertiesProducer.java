package org.djr.eventlog.store;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by djr4488 on 11/15/17.
 */
@ApplicationScoped
public class StoreFactoryPropertiesProducer {
    @Produces
    @StoreFactoryProperties
    public Properties produceStoreFactoryProperties()
    throws IOException {
        Properties prop = new Properties();
        InputStream in = getClass().getClassLoader().getResourceAsStream("storefactory.properties");
        prop.load(in);
        in.close();
        return prop;

    }
}
