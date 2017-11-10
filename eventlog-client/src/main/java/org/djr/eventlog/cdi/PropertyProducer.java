package org.djr.eventlog.cdi;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by djr4488 on 11/4/17.
 */
@ApplicationScoped
public class PropertyProducer {
    @Inject
    private Logger logger;
    private static final String propertyFile = "classpath:eventlog.properties";
    private Properties properties;

    @PostConstruct
    public void loadProperties() {
        logger.debug("loadProperties() - loading propertyFile:{}", propertyFile);
        try {
            properties = new Properties();
            properties.load(new FileInputStream(propertyFile));
        } catch (IOException ioEx) {
            logger.error("loadProperties() - ", ioEx);
            throw new RuntimeException(ioEx);
        }
    }

    @Produces
    public String getStringProperty(InjectionPoint injectionPoint) {
        logger.debug("getProperty()");
        String className = injectionPoint.getMember().getDeclaringClass().getSimpleName();
        String member = injectionPoint.getMember().getName();
        logger.debug("getStringProperty() - {}.{}={}", className, member,
                properties.getProperty(className + "." + member));
        return properties.getProperty(className + "." + member);
    }

    @Produces
    public boolean getBooleanProperty(InjectionPoint injectionPoint) {
        logger.debug("getProperty()");
        String className = injectionPoint.getMember().getDeclaringClass().getSimpleName();
        String member = injectionPoint.getMember().getName();
        logger.debug("getStringProperty() - {}.{}={}", className, member,
                properties.getProperty(className + "." + member));
        return Boolean.valueOf(properties.getProperty(className + "." + member));
    }

    @Produces
    public Integer getIntegerProperty(InjectionPoint injectionPoint) {
        logger.debug("getProperty()");
        String className = injectionPoint.getMember().getDeclaringClass().getSimpleName();
        String member = injectionPoint.getMember().getName();
        logger.debug("getStringProperty() - {}.{}={}", className, member,
                properties.getProperty(className + "." + member));
        return Integer.parseInt(properties.getProperty(className + "." + member));
    }
}
