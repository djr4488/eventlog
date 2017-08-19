package org.djr.eventlog.eventbus;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.djr.eventlog.rest.EventLogRequest;

import java.io.Serializable;

public class EventLogMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private EventLogRequest eventLogRequest;

    public EventLogMessage() {
    }

    public EventLogMessage(EventLogRequest eventLogRequest) {
        this.eventLogRequest = eventLogRequest;
    }

    public EventLogRequest getEventLogRequest() {
        return eventLogRequest;
    }

    public void setEventLogRequest(EventLogRequest eventLogRequest) {
        this.eventLogRequest = eventLogRequest;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
