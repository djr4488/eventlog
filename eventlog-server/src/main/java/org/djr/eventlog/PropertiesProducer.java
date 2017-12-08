package org.djr.eventlog;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.bean.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@ApplicationScoped
public class PropertiesProducer {
    @Produces
    @EventLogProperties
    public Properties loadConfigProperties(InjectionPoint injectionPoint)
    throws IOException {
        EventLogProperties eventlogProperties = injectionPoint.getAnnotated().getAnnotation(EventLogProperties.class);
        String propertyFileName = "eventlog.properties";
        if (null != eventlogProperties && !"".equals(eventlogProperties.name().trim())) {
            propertyFileName = eventlogProperties.name();
        }
        Properties prop = new Properties();
        InputStream in = getClass().getClassLoader().getResourceAsStream(propertyFileName);
        prop.load(in);
        in.close();
        return prop;
    }
}
