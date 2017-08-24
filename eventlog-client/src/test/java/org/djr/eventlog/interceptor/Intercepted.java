package org.djr.eventlog.interceptor;

import org.djr.eventlog.annotations.EventLogParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.Interceptor;
import javax.interceptor.Interceptors;
import javax.persistence.criteria.CriteriaBuilder;

@ApplicationScoped
public class Intercepted {
    private static Logger log = LoggerFactory.getLogger(Intercepted.class);

    @Interceptors(EventLogInterceptor.class)
    public void interceptingMe(String notDataPointed, @EventLogParameter(eventLogParameterName = "dataPointed") String dataPointed, String notDataPointed2,
                               @EventLogParameter(eventLogParameterName = "i") int i) {
        log.debug("interceptingMe() notDataPointed:{}, dataPointed:{}, notDataPointed2:{}, i:{}", notDataPointed,
                dataPointed, notDataPointed2, i);
    }

    @Interceptors(EventLogInterceptor.class)
    public void interceptingStructTypes(@EventLogParameter(scanForEventLogAttributes = true) InterceptedStruct interceptedStruct) {
        log.debug("interceptingStructTypes() interceptedStruct:{}", interceptedStruct);
    }

    @Interceptors(EventLogInterceptor.class)
    public InterceptedReturnStuct interceptingStructWithReturn(@EventLogParameter(scanForEventLogAttributes = true) InterceptedStruct interceptedStruct) {
        log.debug("interceptingStructWithReturn() interceptedStruct:{}", interceptedStruct);
        return new InterceptedReturnStuct("somethingReturned", "sometihngMasked");
    }

    @Interceptors(EventLogInterceptor.class)
    public void interceptingStructWithException(@EventLogParameter(scanForEventLogAttributes = true) InterceptedStruct interceptedStruct) {
        log.debug("interceptingStructWithException() interceptedStruct:{}", interceptedStruct);
        throw new NullPointerException("test message");
    }
}
