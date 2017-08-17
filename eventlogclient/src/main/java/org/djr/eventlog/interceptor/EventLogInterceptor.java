package org.djr.eventlog.interceptor;

import org.djr.eventlog.Configurator;
import org.djr.eventlog.annotations.EventLogParameter;
import org.djr.eventlog.eventbus.EventLogMessage;
import org.djr.eventlog.eventbus.EventLogService;
import org.djr.eventlog.rest.EventLogRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
public class EventLogInterceptor {
    private static Logger log = LoggerFactory.getLogger(EventLogInterceptor.class);
    @Inject
    private EventLogService eventLogService;
    @Inject
    private Configurator configurator;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext invocationContext)
    throws Exception {
        log.debug("intercept() intercepting for method:{}", invocationContext.getMethod().getName());
        eventLogService.publishEventLogMessage(generateEventLog(invocationContext.getMethod(), invocationContext.getParameters()));
        return invocationContext.proceed();
    }

    private EventLogMessage generateEventLog(Method method, Object[] parameters) {
        Map<String, String> dataPointMap = new HashMap<>();
        Annotation[][] annotations = method.getParameterAnnotations();
        dataPointMap.put("methodName", method.getName());
        int idx = 0;
        for (Annotation[] parameterAnnotations : annotations) {
            for (Annotation annotation : parameterAnnotations) {
                    if (EventLogParameter.class.equals(annotation.annotationType())) {
                        dataPointMap.put(method.getParameterTypes()[idx].getName(), parameters[idx].toString());
                    }
            }
            idx++;
        }
        EventLogInterceptorConfig config = configurator.getConfiguration(EventLogInterceptorConfig.class);
        EventLogRequest elr = new EventLogRequest(MDC.get(config.trackingIdentifierKey()), new Date(), MDC.get(config.applicationNameKey()),
                MDC.get(config.environmentKey()), MDC.get(config.serverKey()), "Method Intercept", null, null, dataPointMap);
        EventLogMessage elm = new EventLogMessage(elr);
        log.debug("generateEventLog() eventLogMessage:{}", elm);
        return elm;
    }
}
