package org.djr.eventlog.interceptor;

import org.djr.eventlog.annotations.EventLog;
import org.djr.eventlog.annotations.EventLogParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.Interceptors;

@ApplicationScoped
public class Intercepted {
    private static Logger log = LoggerFactory.getLogger(Intercepted.class);

    @Interceptors({EventLogInterceptor.class})
    @EventLog
    public void interceptingMe(String notDataPointed, @EventLogParameter(name = "dataPointed") String dataPointed, String notDataPointed2,
                               @EventLogParameter(name = "i") int i) {
        log.debug("interceptingMe() notDataPointed:{}, dataPointed:{}, notDataPointed2:{}, i:{}", notDataPointed,
                dataPointed, notDataPointed2, i);
    }

    @Interceptors({EventLogInterceptor.class})
    @EventLog
    public void interceptingStructTypes(@EventLogParameter(scanForEventLogAttributes = true) InterceptedStruct interceptedStruct) {
        log.debug("interceptingStructTypes() interceptedStruct:{}", interceptedStruct);
    }

    @Interceptors({EventLogInterceptor.class})
    @EventLog
    public InterceptedReturnStuct interceptingStructWithReturn(@EventLogParameter(scanForEventLogAttributes = true) InterceptedStruct interceptedStruct) {
        log.debug("interceptingStructWithReturn() interceptedStruct:{}", interceptedStruct);
        return new InterceptedReturnStuct("somethingReturned", "sometihngMasked");
    }

    @Interceptors({EventLogInterceptor.class})
    @EventLog
    public void interceptingStructWithException(@EventLogParameter(scanForEventLogAttributes = true) InterceptedStruct interceptedStruct) {
        log.debug("interceptingStructWithException() interceptedStruct:{}", interceptedStruct);
        throw new NullPointerException("test message");
    }
}
