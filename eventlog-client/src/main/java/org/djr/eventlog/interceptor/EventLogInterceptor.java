package org.djr.eventlog.interceptor;

import org.djr.eventlog.EventLogConstants;
import org.djr.eventlog.annotations.EventLogAttribute;
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RequestScoped
public class EventLogInterceptor {
    private static Logger log = LoggerFactory.getLogger(EventLogInterceptor.class);
    @Inject
    private EventLogService eventLogService;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext invocationContext)
    throws Exception {
        log.debug("intercept() intercepting for method:{}", invocationContext.getMethod().getName());
        Map<String, String> dataPointMap = generateEventLogDataPoints(invocationContext.getMethod(), invocationContext.getParameters());
        Object result = null;
        Exception toThrow = null;
        try {
            result = invocationContext.proceed();
            if (null != result) {
                doGenerateDataPointMapForObject(result, dataPointMap);
            } else {
                dataPointMap.put("Method Intercept Return", "is null");
            }
        } catch (Exception ex) {
            dataPointMap.put("Exception Message", ex.getMessage());
            dataPointMap.put("Exception Type", ex.getClass().getName());
            toThrow = ex;
        }
        EventLogRequest elr = new EventLogRequest(MDC.get(EventLogConstants.eventLogTrackingIdKey), ZonedDateTime.now().toInstant().toEpochMilli(),
                MDC.get(EventLogConstants.eventLogApplicationNameKey), null, MDC.get(EventLogConstants.eventLogServerKey),
                "Method Intercept", null, null, dataPointMap);
        EventLogMessage elm = new EventLogMessage(elr);
        eventLogService.publishEventLogMessage(elm);
        if (null != toThrow) {
            throw toThrow;
        }
        return result;
    }

    private Map<String, String> generateEventLogDataPoints(Method method, Object[] parameters) {
        Map<String, String> dataPointMap = new HashMap<>();
        try {
            Annotation[][] annotations = method.getParameterAnnotations();
            dataPointMap.put("methodName", method.getName());
            int idx = 0;
            for (Annotation[] parameterAnnotations : annotations) {
                for (Annotation annotation : parameterAnnotations) {
                    if (EventLogParameter.class.equals(annotation.annotationType())) {
                        EventLogParameter elp = (EventLogParameter) annotation;
                        dataPointMap.put("Parameter parsing", dataPointKeyForParameterName(elp.eventLogParameterName(), parameters[idx].getClass().getName()));
                        if (elp.scanForEventLogAttributes()) {
                            dataPointMap = doGenerateDataPointMapForObject(parameters[idx], dataPointMap);
                        } else {
                            dataPointMap.put(method.getParameterTypes()[idx].getName(), parameters[idx].toString());
                        }
                    }
                }
                idx++;
            }
        } catch (Exception ex) {
            dataPointMap.put("Failed mapping data points for eventlog parameter", ex.getMessage());
            log.error("generateEventLogDataPoints() ", ex);
        }
        return dataPointMap;
    }

    private Map<String, String> doGenerateDataPointMapForObject(Object object, Map<String, String> dataPointMap) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(EventLogAttribute.class)) {
                    EventLogAttribute eventLogAttribute = field.getAnnotation(EventLogAttribute.class);
                    field.setAccessible(true);
                    if (eventLogAttribute.maskAttribute()) {
                        dataPointMap.put(dataPointKeyForParameterName(eventLogAttribute.attributeName(), field.getName()), "*");
                    } else {
                        dataPointMap.put(dataPointKeyForParameterName(eventLogAttribute.attributeName(), field.getName()), field.get(object).toString());
                    }
                } else {
                    dataPointMap.put(field.getName(), field.toString());
                }
            }
        } catch (Exception ex) {
            dataPointMap.put("Failed mapping field(s)", ex.getMessage());
            log.error("doGenerateDataPointMapForResult() object:{}", object);
        }
        return dataPointMap;
    }

    private String dataPointKeyForParameterName(String eventLogParameterName, String fieldName) {
        return (null != eventLogParameterName && !"".equals(eventLogParameterName.trim())) ? eventLogParameterName : fieldName;
    }
}
