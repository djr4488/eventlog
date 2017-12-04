package org.djr.eventlog.provider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * Created by djr4488 on 12/3/17.
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class JacksonJsonProvider extends com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider {
}
