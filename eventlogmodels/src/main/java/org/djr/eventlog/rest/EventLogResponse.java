package org.djr.eventlog.rest;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class EventLogResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean storedSuccessfully;

    public EventLogResponse() {
    }

    public EventLogResponse(boolean storedSuccessfully) {
        this.storedSuccessfully = storedSuccessfully;
    }

    public boolean isStoredSuccessfully() {
        return storedSuccessfully;
    }

    public void setStoredSuccessfully(boolean storedSuccessfully) {
        this.storedSuccessfully = storedSuccessfully;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
