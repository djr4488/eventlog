package org.djr.eventlog;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by djr4488 on 12/4/17.
 */
@ApplicationScoped
public class EventLogPropertiesProducer {
    @Produces
    @EventLogProperties
    public Properties loadConfigProperties()
    throws IOException {
        Properties prop = new Properties();
        InputStream in = getClass().getClassLoader().getResourceAsStream("eventlog.properties");
        prop.load(in);
        in.close();
        return prop;
    }
}
