package org.djr.eventlog.rest;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class EventLogRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String trackingIdentifier;
    private Date eventOccurredAt;
    private String applicationName;
    private String environment;
    private String server;
    private String eventCode;
    private String businessErrorCode;
    private String systemErrorCode;
    private Map<String, String> dataPoints;

    public EventLogRequest() {
    }

    public EventLogRequest(String trackingIdentifier, Date eventOccurredAt, String applicationName, String environment, String server, String eventCode,
                           String businessErrorCode, String systemErrorCode, Map<String, String> dataPoints) {
        this.trackingIdentifier = trackingIdentifier;
        this.eventOccurredAt = eventOccurredAt;
        this.applicationName = applicationName;
        this.environment = environment;
        this.server = server;
        this.eventCode = eventCode;
        this.businessErrorCode = businessErrorCode;
        this.systemErrorCode = systemErrorCode;
        this.dataPoints = dataPoints;
    }

    public String getTrackingIdentifier() {
        return trackingIdentifier;
    }

    public void setTrackingIdentifier(String trackingIdentifier) {
        this.trackingIdentifier = trackingIdentifier;
    }

    public Date getEventOccurredAt() {
        return eventOccurredAt;
    }

    public void setEventOccurredAt(Date eventOccurredAt) {
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

    public String getBusinessErrorCode() {
        return businessErrorCode;
    }

    public void setBusinessErrorCode(String businessErrorCode) {
        this.businessErrorCode = businessErrorCode;
    }

    public String getSystemErrorCode() {
        return systemErrorCode;
    }

    public void setSystemErrorCode(String systemErrorCode) {
        this.systemErrorCode = systemErrorCode;
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
