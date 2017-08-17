package org.djr.eventlog.rest;

import retrofit2.Call;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

import java.util.Map;

public interface EventLogTransport {
    @POST("api/eventlog/")
    Call<EventLogResponse> postEventLog(@HeaderMap Map<String, String> headerMap, EventLogRequest eventLogRequest);
}
