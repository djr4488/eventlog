package org.djr.eventlog.interceptor;

import org.djr.eventlog.annotations.EventLogParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.Interceptors;

@ApplicationScoped
public class Intercepted {
    private static Logger log = LoggerFactory.getLogger(Intercepted.class);

    @Interceptors(EventLogInterceptor.class)
    public void interceptingMe(String notDataPointed, @EventLogParameter String dataPointed, String notDataPointed2,
                               @EventLogParameter int i) {
        log.debug("interceptingMe() notDataPointed:{}, dataPointed:{}, notDataPointed2:{}, i:{}", notDataPointed,
                dataPointed, notDataPointed2, i);
    }

    @Interceptors(EventLogInterceptor.class)
    public void interceptingStructTypes(@EventLogParameter InterceptedStruct interceptedStruct) {
        log.debug("intceptingStructTypes() interceptedStruct:{}", interceptedStruct);
    }
}
