package org.djr.eventlog.store;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "data_points")
public class DataPoint extends Identifiable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "data_point_name")
    private String dataPointName;
    @Column(name = "data_point_value", length = 30000)
    @Lob
    private String dataPointValue;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_logs_id")
    private EventLog eventLog;

    public DataPoint() {
    }

    public DataPoint(String dataPointName, String dataPointValue, EventLog eventLog) {
        this.dataPointName = dataPointName;
        this.dataPointValue = dataPointValue;
        this.eventLog = eventLog;
    }

    public String getDataPointName() {
        return dataPointName;
    }

    public void setDataPointName(String dataPointName) {
        this.dataPointName = dataPointName;
    }

    public String getDataPointValue() {
        return dataPointValue;
    }

    public void setDataPointValue(String dataPointValue) {
        this.dataPointValue = dataPointValue;
    }

    public EventLog getEventLog() {
        return eventLog;
    }

    public void setEventLog(EventLog eventLog) {
        this.eventLog = eventLog;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
