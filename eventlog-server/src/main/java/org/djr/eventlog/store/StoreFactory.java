package org.djr.eventlog.store;

import org.djr.eventlog.store.elasticsearch.ElasticStoreService;
import org.djr.eventlog.store.jpa.EventLogJpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.util.Properties;

/**
 * Created by djr4488 on 11/15/17.
 */
@ApplicationScoped
public class StoreFactory {
    private static Logger log = LoggerFactory.getLogger(StoreFactory.class);
    @Inject
    @StoreFactoryProperties
    private Properties properties;

    @Produces
    @EventLogStoreProducer
    public EventLogStore produceEventLogStore(InjectionPoint injectionPoint) {
        log.debug("produceEventLogStore() starting injection of eventlog store service");
        StoreConfig storeConfig = injectionPoint.getAnnotated().getAnnotation(StoreConfig.class);
        String storeType = properties.getProperty(storeConfig.storeTypePropertyName());
        EventLogStore store = null;
        try {
            switch (storeType) {
                case "JPA": {
                    store = getBeanByNameOfClass(storeType, EventLogJpaService.class);
                    break;
                }
                case "ELASTIC": {
                    store = getBeanByNameOfClass(storeType, ElasticStoreService.class);
                    break;
                }
                default: {
                    throw new RuntimeException("Failed to produce event log store service since no match");
                }
            }
        } catch (Exception ex) {
            log.error("produceEventLogStore() failed with exception", ex);
        }
        return store;
    }

    /**
     * Method to help classes not managed by CDI to get CDI managed beans
     * @param name name of the bean you are looking for; should be annotated with @Named("beanName")
     * @param clazz the class type you are looking for BeanName.class
     * @param <T> generics
     * @return instance of BeanName.class
     * @throws Exception if something goes horribly wrong I suppose it could get thrown
     */
    private <T> T getBeanByNameOfClass(String name, Class<T> clazz)
    throws Exception {
        BeanManager bm = CDI.current().getBeanManager();
        Bean<T> bean = (Bean<T>) bm.getBeans(name).iterator().next();
        CreationalContext<T> ctx = bm.createCreationalContext(bean);
        return (T) bm.getReference(bean, clazz, ctx);
    }
}
