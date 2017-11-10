package org.djr.eventlog.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.djr.eventlog.Configurator;
import org.djr.eventlog.EventLogClientException;
import org.apache.commons.codec.binary.Base64;
import org.djr.retrofit2ee.json.DeserializationFeatureConfig;
import org.djr.retrofit2ee.json.JacksonDeserializationFeature;
import org.djr.retrofit2ee.json.JacksonMapperFeature;
import org.djr.retrofit2ee.json.JacksonModule;
import org.djr.retrofit2ee.json.JacksonSerializationFeature;
import org.djr.retrofit2ee.json.MapperFeatureConfig;
import org.djr.retrofit2ee.json.RetrofitJson;
import org.djr.retrofit2ee.json.RetrofitJsonConfig;
import org.djr.retrofit2ee.json.SerializationFeatureConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@ApplicationScoped
public class EventLogClient {
    private static Logger log = LoggerFactory.getLogger(EventLogClient.class);
    @Inject
    private Configurator configurator;
    //private EventLogClientConfig config = configurator.getConfiguration(EventLogClientConfig.class);

    @RetrofitJsonConfig(captureTrafficLogsPropertyName = "EventLogClient.enableTrafficLogging",
            baseUrlPropertyName = "EventLogClient.baseUrl")
    @JacksonModule(jacksonModules = {com.fasterxml.jackson.datatype.jsr310.JavaTimeModule.class})
    @JacksonMapperFeature(features = {
            @MapperFeatureConfig(feature = MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, value = false),
            @MapperFeatureConfig(feature = MapperFeature.AUTO_DETECT_GETTERS, value = true)})
    @JacksonSerializationFeature(features = {
            @SerializationFeatureConfig(feature = SerializationFeature.INDENT_OUTPUT, value = true)})
    @JacksonDeserializationFeature(features = {
            @DeserializationFeatureConfig(feature = DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, value = false)})
    @Inject
    @RetrofitJson
    private Retrofit retrofitJson;

    @PostConstruct
    public void postConstruct() {
    }

    public EventLogResponse doPostEventLogRequest(EventLogRequest eventLogRequest) {
        log.debug("doPostEventLogRequest() eventLogRequest:{}", eventLogRequest);
        try {
            Response<EventLogResponse> response = retrofitJson.create(EventLogTransport.class).postEventLog(getHeaderMap(), eventLogRequest).execute();
            if (!response.isSuccessful()) {
                log.error("doPostEventLogRequest() failed with errorBody:{} and status:{}", response.errorBody(), response.code());
                throw new EventLogClientException("Failed with status:" + response.code());
            }
            log.debug("doPostEventLogRequest() response.body():{}", response.body());
            return response.body();
        } catch (IOException ioEx) {
            throw new EventLogClientException("Failed calling endpoint", ioEx);
        }
    }

    private Map<String, String> getHeaderMap() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("content-type", MediaType.APPLICATION_JSON);
        headerMap.put("accept", MediaType.APPLICATION_JSON);
//        if (null != config.username() && null != config.password()) {
//            String userNameAndPassword = config.username() + ":" + config.password();
//            headerMap.put("Authorization", "Bearer " + Base64.encodeBase64String(userNameAndPassword.getBytes()));
//        }
        return headerMap;
    }
}
