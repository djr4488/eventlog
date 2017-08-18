package org.djr.eventlog.rest;

import org.djr.eventlog.Configurator;
import org.djr.eventlog.TransportProducer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import static com.fasterxml.jackson.databind.MapperFeature.AUTO_DETECT_GETTERS;
import static com.fasterxml.jackson.databind.MapperFeature.REQUIRE_SETTERS_FOR_GETTERS;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

@ApplicationScoped
public class EventLogTransportProducer {
    private static Logger log = LoggerFactory.getLogger(EventLogTransportProducer.class);
    @Inject
    private Configurator configurator;
    private EventLogTransportConfig config;

    @PostConstruct
    public void postContruct() {
        config = configurator.getConfiguration(EventLogTransportConfig.class);
    }

    @Produces
    public EventLogTransport getEventLogTransport() {
        return TransportProducer.getTransport(getObjectMapper(), config.baseUrl()).create(EventLogTransport.class);
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.configure(REQUIRE_SETTERS_FOR_GETTERS, false);
        objectMapper.configure(AUTO_DETECT_GETTERS, true);
        objectMapper.configure(INDENT_OUTPUT, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
