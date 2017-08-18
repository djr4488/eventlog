package org.djr.eventlog.rest;

import retrofit2.Call;
import retrofit2.http.HeaderMap;
import retrofit2.mock.BehaviorDelegate;

import java.util.Map;

/**
 * Created by djr4488 on 8/17/17.
 */
public class MockEventLogEndpoint implements EventLogTransport {
    private final BehaviorDelegate<EventLogTransport> behaviorDelegate;

    public MockEventLogEndpoint(BehaviorDelegate<EventLogTransport> behaviorDelegate) {
        this.behaviorDelegate = behaviorDelegate;
    }

    @Override
    public Call<EventLogResponse> postEventLog(@HeaderMap Map<String, String> headerMap, EventLogRequest eventLogRequest) {
        EventLogResponse elr = new EventLogResponse(true);
        return behaviorDelegate.returningResponse(elr).postEventLog(headerMap, eventLogRequest);
    }
}
