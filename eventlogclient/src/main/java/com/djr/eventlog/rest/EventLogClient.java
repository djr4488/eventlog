package com.djr.eventlog.rest;

import com.djr.eventlog.Configurator;
import com.djr.eventlog.EventLogClientException;
import org.apache.commons.codec.binary.Base64;
import org.djr.eventlog.rest.EventLogRequest;
import org.djr.eventlog.rest.EventLogResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;

@ApplicationScoped
public class EventLogClient {
    private static Logger log = LoggerFactory.getLogger(EventLogClient.class);
    @Inject
    private EventLogTransport eventLogTransport;
    @Inject
    private Configurator configurator;
    private EventLogClientConfig config;

    @PostConstruct
    public void postConstruct() {
        config = configurator.getConfiguration(EventLogClientConfig.class);
    }

    public void doPostEventLogRequest(EventLogRequest eventLogRequest) {
        try {
            Response<EventLogResponse> response = eventLogTransport.postEventLog(getHeaderMap(), eventLogRequest).execute();
            if (!response.isSuccessful()) {
                log.error("doPostEventLogRequest() failed with errorBody:{} and status:{}", response.errorBody(), response.code());
                throw new EventLogClientException("Failed with status:" + response.code());
            }
            log.debug("doPostEventLogRequest() response.body():{}", response.body());
        } catch (IOException ioEx) {
            throw new EventLogClientException("Failed calling endpoint", ioEx);
        }
    }

    public Map<String, String> getHeaderMap() {
        Map<String, String> headerMap = null;
        headerMap.put("content-type", config.contentType());
        headerMap.put("accept", config.accept());
        if (null != config.username() && null != config.password()) {
            String userNameAndPassword = config.username() + ":" + config.password();
            headerMap.put("Authorization", "Bearer " + Base64.encodeBase64String(userNameAndPassword.getBytes()));
        }
        return headerMap;
    }
}
