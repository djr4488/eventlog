package org.djr.eventlog;

import io.swagger.jaxrs.listing.ApiListingResource;
import org.djr.eventlog.rest.EventLogEndpoint;
import org.djr.eventlog.rest.EventLogExceptionMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
@ApplicationPath("api")
public class EventLogApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList(EventLogEndpoint.class, EventLogExceptionMapper.class, ApiListingResource.class));
    }
}
