package org.djr.eventlog.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by djr4488 on 11/12/17.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class EventResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlElement(name = "tracking_identifier")
    private String trackingId;

    public EventResponse(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }
}
