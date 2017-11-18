package org.djr.eventlog.rest;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.djr.eventlog.EventLogController;
import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@ApplicationScoped
@Path("eventlog")
@Api("EventLogEndpoint")
public class EventLogEndpoint {
    private static Logger log = LoggerFactory.getLogger(EventLogEndpoint.class);
    @Inject
    private EventLogController eventLogController;

    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @POST
    @Path("store")
    @ApiOperation(value ="store", notes = "Used for posting Event Logs to be stored")
    public EventLogResponse postEventLogRequest(EventLogRequest eventLogRequest) {
        log.info("postEventLogRequest() eventLogRequest:{}", eventLogRequest);
        eventLogController.doHandleEventLogRequest(eventLogRequest);
        return new EventLogResponse(true);
    }

    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @GET
    @Path("search/{trackingId}")
    @ApiOperation(value ="search/{trackingId}", notes = "Allows to retrieve all events for a specific tracking identifier")
    public SearchResponse getEventsByTrackingId(@Context HttpServletRequest request,
                                                @PathParam("trackingId") String trackingId)
    throws IOException {
        log.info("getEventsByTrackingId() entered trackingId:{}", trackingId);
        //temporary just learning elastic search
        return eventLogController.doSearch("{\"query\":{\"match\":{\"tracking_identifier\":\""+trackingId+"\"}}}");
    }


}
