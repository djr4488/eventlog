package org.djr.eventlog.rest;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Map;

public class EventLogRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String trackingIdentifier;
    private Long eventOccurredAt;
    private String applicationName;
    private String environment;
    private String server;
    private String eventCode;
    private String errorCode;
    private String eventType;
    private Boolean alertOnBusinessException;
    private Boolean alertOnSystemException;
    private Long executeTime;
    private Long interceptExecuteTime;
    private String exceptionType;
    private Map<String, String> dataPoints;

    public EventLogRequest() {
    }

    public EventLogRequest(String trackingIdentifier, Long eventOccurredAt, String applicationName, String environment, String server, String eventCode,
                           String errorCode, String eventType, Boolean alertOnBusinessException, Boolean alertOnSystemException, Long executeTime,
                           Long interceptExecuteTime, Map<String, String> dataPoints) {
        this.trackingIdentifier = trackingIdentifier;
        this.eventOccurredAt = eventOccurredAt;
        this.applicationName = applicationName;
        this.environment = environment;
        this.server = server;
        this.eventCode = eventCode;
        this.errorCode = errorCode;
        this.eventType = eventType;
        this.alertOnBusinessException = alertOnBusinessException;
        this.alertOnSystemException = alertOnSystemException;
        this.executeTime = executeTime;
        this.interceptExecuteTime = interceptExecuteTime;
        this.dataPoints = dataPoints;
    }

    public String getTrackingIdentifier() {
        return trackingIdentifier;
    }

    public void setTrackingIdentifier(String trackingIdentifier) {
        this.trackingIdentifier = trackingIdentifier;
    }

    public Long getEventOccurredAt() {
        return eventOccurredAt;
    }

    public void setEventOccurredAt(Long eventOccurredAt) {
        this.eventOccurredAt = eventOccurredAt;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Boolean getAlertOnBusinessException() {
        return alertOnBusinessException;
    }

    public void setAlertOnBusinessException(Boolean alertOnBusinessException) {
        this.alertOnBusinessException = alertOnBusinessException;
    }

    public Boolean getAlertOnSystemException() {
        return alertOnSystemException;
    }

    public void setAlertOnSystemException(Boolean alertOnSystemException) {
        this.alertOnSystemException = alertOnSystemException;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public Long getInterceptExecuteTime() {
        return interceptExecuteTime;
    }

    public void setInterceptExecuteTime(Long interceptExecuteTime) {
        this.interceptExecuteTime = interceptExecuteTime;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public Map<String, String> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(Map<String, String> dataPoints) {
        this.dataPoints = dataPoints;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
