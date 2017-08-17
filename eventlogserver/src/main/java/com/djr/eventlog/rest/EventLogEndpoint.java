package com.djr.eventlog.rest;



import com.djr.eventlog.EventLogController;
import org.djr.eventlog.rest.EventLogRequest;
import org.djr.eventlog.rest.EventLogResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class EventLogEndpoint {
    private static Logger log = LoggerFactory.getLogger(EventLogEndpoint.class);
    @Inject
    private EventLogController eventLogController;

    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @POST
    @Path("/eventlog")
    public EventLogResponse postEventLogRequest(EventLogRequest eventLogRequest) {
        log.info("postEventLogRequest() eventLogRequest:{}", eventLogRequest);
        eventLogController.storeEventLogRequest(eventLogRequest);
        return new EventLogResponse(true);
    }
}
