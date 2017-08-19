package org.djr.eventlog.rest;



import org.djr.eventlog.EventLogController;
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
    @Path("/store")
    public EventLogResponse postEventLogRequest(EventLogRequest eventLogRequest) {
        log.info("postEventLogRequest() eventLogRequest:{}", eventLogRequest);
        eventLogController.doHandleEventLogRequest(eventLogRequest);
        return new EventLogResponse(true);
    }
}
