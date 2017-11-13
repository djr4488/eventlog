package org.djr.eventlog.store;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "event_logs",
        indexes = {
                @Index(name = "idx_event_occurred_at", columnList = "event_occurred_at"),
                @Index(name = "idx_application_name", columnList = "application_name"),
                @Index(name = "idx_event_code", columnList = "event_code"),
                @Index(name = "idx_business_error_code", columnList = "business_error_code"),
                @Index(name = "idx_system_error_code", columnList = "system_error_code"),
                @Index(name = "idx_tracking_identifier", columnList = "tracking_identifier")})
@NamedQueries({
        @NamedQuery(name = "findEventLogsByTrackingIdentifier",
            query = "select eventlog from EventLog eventLog where eventLog.trackingIdentifier = :trackingIdentifier")
})
public class EventLog extends Identifiable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "event_occurred_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventOccurredAt;
    @Column(name = "application_name")
    private String applicationName;
    @Column(name = "environment")
    private String environment;
    @Column(name = "server")
    private String server;
    @Column(name = "event_code")
    private String eventCode;
    @Column(name = "business_error_code")
    private String businessErrorCode;
    @Column(name = "system_error_code")
    private String systemErrorCode;
    @Column(name = "tracking_identifier")
    private String trackingIdentifier;
    @Column(name = "alert_on_error")
    private Boolean alertOnError;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "EventLog", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<DataPoint> dataPoint;

    public EventLog() {
    }

    public EventLog(Date eventOccurredAt, String applicationName, String environment, String server, String eventCode,
                    String businessErrorCode, String systemErrorCode, String trackingIdentifier, Boolean alertOnError,
                    List<DataPoint> dataPoint) {
        this.eventOccurredAt = eventOccurredAt;
        this.applicationName = applicationName;
        this.environment = environment;
        this.server = server;
        this.eventCode = eventCode;
        this.businessErrorCode = businessErrorCode;
        this.systemErrorCode = systemErrorCode;
        this.trackingIdentifier = trackingIdentifier;
        this.alertOnError = alertOnError;
        this.dataPoint = dataPoint;
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

    public String getTrackingIdentifier() {
        return trackingIdentifier;
    }

    public void setTrackingIdentifier(String trackingIdentifier) {
        this.trackingIdentifier = trackingIdentifier;
    }

    public Boolean getAlertOnError() {
        return alertOnError;
    }

    public void setAlertOnError(Boolean alertOnError) {
        this.alertOnError = alertOnError;
    }

    public List<DataPoint> getDataPoint() {
        return dataPoint;
    }

    public void setDataPoint(List<DataPoint> dataPoint) {
        this.dataPoint = dataPoint;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
