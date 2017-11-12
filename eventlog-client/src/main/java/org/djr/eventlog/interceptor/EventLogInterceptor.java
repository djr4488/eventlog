package org.djr.eventlog.interceptor;

import org.djr.eventlog.EventController;
import org.djr.eventlog.EventLogConstants;
import org.djr.eventlog.annotations.EventLog;
import org.djr.eventlog.annotations.EventLogAttribute;
import org.djr.eventlog.annotations.EventLogParameter;
import org.djr.eventlog.eventbus.EventLogMessage;
import org.djr.eventlog.rest.EventLogRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.annotation.Priority;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventLog
@Interceptor
@Priority(1010)
public class EventLogInterceptor {
    private static Logger log = LoggerFactory.getLogger(EventLogInterceptor.class);
    @Inject
    private EventController eventController;
    @Resource(lookup="java:app/AppName")
    private String resourceAppName;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext invocationContext)
    throws Exception {
        log.debug("intercept() intercepting for method:{}", invocationContext.getMethod().getName());
        EventLog eventLog = invocationContext.getMethod().getAnnotation(EventLog.class);
        createTrackingForEventLogIfRequired(eventLog);
        Map<String, String> dataPointMap = generateEventLogDataPoints(invocationContext.getMethod(), invocationContext.getParameters());
        Object result = null;
        Exception toThrow = null;
        try {
            result = invocationContext.proceed();
        } catch (Exception ex) {
            dataPointMap.put("Exception Message", ex.getMessage());
            dataPointMap.put("Exception Type", ex.getClass().getName());
            toThrow = ex;
        }
        createDataPointForMethodIntercept(dataPointMap, result);
        createAndPublishEventLog(eventLog, dataPointMap);
        if (null != toThrow) {
            throw toThrow;
        }
        return result;
    }

    private void createDataPointForMethodIntercept(Map<String, String> dataPointMap, Object result) {
        if (null != result) {
            doGenerateDataPointMapForObject(result, dataPointMap);
        } else {
            dataPointMap.put("Method Intercept Return", "is null");
        }
    }

    private void createAndPublishEventLog(EventLog eventLog, Map<String, String> dataPointMap) {
        EventLogRequest elr = new EventLogRequest(MDC.get(EventLogConstants.eventLogTrackingIdKey), ZonedDateTime.now().toInstant().toEpochMilli(),
                MDC.get(EventLogConstants.eventLogApplicationNameKey), null, MDC.get(EventLogConstants.eventLogServerKey),
                "Method Intercept", null, null, eventLog.alertOnException(), dataPointMap);
        EventLogMessage elm = new EventLogMessage(elr);
        eventController.publishEventLogMessage(elm);
    }

    private void createTrackingForEventLogIfRequired(EventLog eventLog) {
        if (eventLog.generateTrackingIdForEntry()) {
            MDC.put(EventLogConstants.eventLogTrackingIdKey, UUID.randomUUID().toString());
            MDC.put(EventLogConstants.eventLogApplicationNameKey, resourceAppName);
            MDC.put(EventLogConstants.eventLogServerKey, getServerInfo());
        }
    }

    private Map<String, String> generateEventLogDataPoints(Method method, Object[] parameters) {
        Map<String, String> dataPointMap = new HashMap<>();
        try {
            Annotation[][] annotations = method.getParameterAnnotations();
            dataPointMap.put("methodName", method.getName());
            int idx = 0;
            for (Annotation[] parameterAnnotations : annotations) {
                for (Annotation annotation : parameterAnnotations) {
                    dataPointMap = createDataPointMapForObjectAttributes(method, parameters, dataPointMap, idx, annotation);
                }
                idx++;
            }
        } catch (Exception ex) {
            dataPointMap.put("Failed mapping data points for eventlog parameter", ex.getMessage());
            log.error("generateEventLogDataPoints() ", ex);
        }
        return dataPointMap;
    }

    private Map<String, String> createDataPointMapForObjectAttributes(Method method, Object[] parameters, Map<String, String> dataPointMap, int idx, Annotation annotation) {
        if (EventLogParameter.class.equals(annotation.annotationType())) {
			EventLogParameter elp = (EventLogParameter) annotation;
			dataPointMap.put("Parameter parsing", dataPointKeyForParameterName(elp.name(), parameters[idx].getClass().getName()));
			if (elp.scanForEventLogAttributes()) {
				dataPointMap = doGenerateDataPointMapForObject(parameters[idx], dataPointMap);
			} else {
				dataPointMap.put(method.getParameterTypes()[idx].getName(), parameters[idx].toString());
			}
		}
        return dataPointMap;
    }

    private Map<String, String> doGenerateDataPointMapForObject(Object object, Map<String, String> dataPointMap) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            boolean fieldAccessibleFlagChanged = false;
            for (Field field : fields) {
                try {
                    if (!isFieldAJavaCollectionOrArray(field)) {
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                            fieldAccessibleFlagChanged = true;
                        }
                        generateDataPointMapForObjectField(object, dataPointMap, field);
                    } else {
                        log.trace("doGenerateDataPointMapForObject() currently collection capture is not supported");
                        int collectionSize = 0;
                        Object objectField = null;
                        try {
                            objectField = field.get(object);
                            collectionSize = objectField != null ? ((Collection) objectField).size() : 0;
                        } catch (Exception ex) {
                            log.debug("doGenerateDataPointMapForObject() failed with exception field:{}", objectField);
                        }
                        dataPointMap.put(field.getName(), "is collection or array, not parsing, but size is " + collectionSize);
                    }
                } catch (Exception ex) {
                    dataPointMap.put("Failed mapping field(s)", ex.getMessage());
                    log.error("doGenerateDataPointMapForResult() object:{}", object, ex);
                } finally {
                    if (fieldAccessibleFlagChanged) {
                        field.setAccessible(false);
                    }
                }
            }
        } catch (Exception ex) {
            dataPointMap.put("Failed mapping field(s)", ex.getMessage());
            log.error("doGenerateDataPointMapForResult() object:{}", object);
        }
        return dataPointMap;
    }

    private boolean isFieldAJavaCollectionOrArray(Field field) {
        return Collections.class.isAssignableFrom(field.getType()) || field.getType().isArray();
    }

    private void generateDataPointMapForObjectField(Object object, Map<String, String> dataPointMap, Field field)
    throws IllegalAccessException {
        if (field.isAnnotationPresent(EventLogAttribute.class)) {
			EventLogAttribute eventLogAttribute = field.getAnnotation(EventLogAttribute.class);
			if (eventLogAttribute.maskAttribute()) {
				dataPointMap.put(dataPointKeyForParameterName(eventLogAttribute.attributeName(), field.getName()), "*");
			} else {
				dataPointMap.put(dataPointKeyForParameterName(eventLogAttribute.attributeName(), field.getName()), field.get(object).toString());
			}
		} else {
			if (null != field.get(object)) {
				dataPointMap.put(field.getName(), field.get(object).toString());
			} else {
				dataPointMap.put(field.getName(), "null");
			}
		}
    }

    private String dataPointKeyForParameterName(String eventLogParameterName, String fieldName) {
        return (null != eventLogParameterName && !"".equals(eventLogParameterName.trim())) ? eventLogParameterName : fieldName;
    }

    private String getServerInfo() {
        String appName = null;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            appName = inetAddress.getHostName() + " - " + inetAddress.getHostAddress();
        } catch (UnknownHostException uhEx) {
            log.debug("getServerInfo() unable to get server info for naming");
        }
        return appName;
    }
}
