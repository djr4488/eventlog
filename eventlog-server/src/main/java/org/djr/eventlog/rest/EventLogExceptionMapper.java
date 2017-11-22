package org.djr.eventlog.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class EventLogExceptionMapper implements ExceptionMapper<Exception> {
    private static final Logger log = LoggerFactory.getLogger(EventLogExceptionMapper.class);
    @Override
    public Response toResponse(Exception ex) {
        log.info("toResponse() completed with errors");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("An unexpected error occurred tx_id:" + MDC.get("tx_id"))
                .build();
    }
}
