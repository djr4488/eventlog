package org.djr.eventlog.rest;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.djr.eventlog.Configurator;
import org.djr.eventlog.EventLogClientException;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.Call;
import retrofit2.Response;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(CdiRunner.class)
public class EventLogClientTest {
    @Produces
    @Mock
    private EventLogTransport eventLogTransport;
    @Produces
    @Mock
    private Configurator configurator;
    @Inject
    private EventLogClient eventLogClient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testEventLogClientSuccessPath() {
        EventLogClientConfig config = mock(EventLogClientConfig.class);
        when(configurator.getConfiguration(EventLogClientConfig.class)).thenReturn(config);
        when(config.contentType()).thenReturn("application/json");
        when(config.accept()).thenReturn("application/json");
        when(config.username()).thenReturn(null);
        when(config.password()).thenReturn(null);
        Call<EventLogResponse> call = mock(Call.class);
        Response<EventLogResponse> response = Response.success(new EventLogResponse(true));
        when(eventLogTransport.postEventLog(anyMap(), any(EventLogRequest.class))).thenReturn(call);
        try {
            when(call.execute()).thenReturn(response);
        } catch (Exception ex) {
            fail("did not expect exception here");
        }
        EventLogResponse elr = eventLogClient.doPostEventLogRequest(new EventLogRequest("id", new Date(), "app", "env", "svr", "tc", null, null, null));
        assertTrue(elr.isStoredSuccessfully());
    }

    @Test(expected = EventLogClientException.class)
    public void testEventLogClientUnsuccessfulPath() {
        EventLogClientConfig config = mock(EventLogClientConfig.class);
        when(configurator.getConfiguration(EventLogClientConfig.class)).thenReturn(config);
        when(config.contentType()).thenReturn("application/json");
        when(config.accept()).thenReturn("application/json");
        when(config.username()).thenReturn(null);
        when(config.password()).thenReturn(null);
        Call<EventLogResponse> call = mock(Call.class);
        Response<EventLogResponse> response = Response.error(403, ResponseBody.create(MediaType.parse("application/json"), "Unauthorized"));
        when(eventLogTransport.postEventLog(anyMap(), any(EventLogRequest.class))).thenReturn(call);
        try {
            when(call.execute()).thenReturn(response);
        } catch (Exception ex) {
            fail("did not expect exception here");
        }
        EventLogResponse elr = eventLogClient.doPostEventLogRequest(new EventLogRequest("id", new Date(), "app", "env", "svr", "tc", null, null, null));
    }

    @Test(expected = EventLogClientException.class)
    public void testEventLogClientIoExceptionPath() {
        EventLogClientConfig config = mock(EventLogClientConfig.class);
        when(configurator.getConfiguration(EventLogClientConfig.class)).thenReturn(config);
        when(config.contentType()).thenReturn("application/json");
        when(config.accept()).thenReturn("application/json");
        when(config.username()).thenReturn(null);
        when(config.password()).thenReturn(null);
        Call<EventLogResponse> call = mock(Call.class);
        when(eventLogTransport.postEventLog(anyMap(), any(EventLogRequest.class))).thenReturn(call);
        try {
            when(call.execute()).thenThrow(new IOException("test exception"));
        } catch (Exception ex) {
            fail("did not expect exception here");
        }
        EventLogResponse elr = eventLogClient.doPostEventLogRequest(new EventLogRequest("id", new Date(), "app", "env", "svr", "tc", null, null, null));
    }

    @Test
    public void testWhenUserAddedButNoPassword() {
        EventLogClientConfig config = mock(EventLogClientConfig.class);
        when(configurator.getConfiguration(EventLogClientConfig.class)).thenReturn(config);
        when(config.contentType()).thenReturn("application/json");
        when(config.accept()).thenReturn("application/json");
        when(config.username()).thenReturn("user");
        when(config.password()).thenReturn(null);
        Call<EventLogResponse> call = mock(Call.class);
        Response<EventLogResponse> response = Response.success(new EventLogResponse(true));
        when(eventLogTransport.postEventLog(anyMap(), any(EventLogRequest.class))).thenReturn(call);
        try {
            when(call.execute()).thenReturn(response);
        } catch (Exception ex) {
            fail("did not expect exception here");
        }
        EventLogResponse elr = eventLogClient.doPostEventLogRequest(new EventLogRequest("id", new Date(), "app", "env", "svr", "tc", null, null, null));
        assertTrue(elr.isStoredSuccessfully());
    }

    @Test
    public void testWhenPasswordButNoUser() {
        EventLogClientConfig config = mock(EventLogClientConfig.class);
        when(configurator.getConfiguration(EventLogClientConfig.class)).thenReturn(config);
        when(config.contentType()).thenReturn("application/json");
        when(config.accept()).thenReturn("application/json");
        when(config.username()).thenReturn(null);
        when(config.password()).thenReturn("pass");
        Call<EventLogResponse> call = mock(Call.class);
        Response<EventLogResponse> response = Response.success(new EventLogResponse(true));
        when(eventLogTransport.postEventLog(anyMap(), any(EventLogRequest.class))).thenReturn(call);
        try {
            when(call.execute()).thenReturn(response);
        } catch (Exception ex) {
            fail("did not expect exception here");
        }
        EventLogResponse elr = eventLogClient.doPostEventLogRequest(new EventLogRequest("id", new Date(), "app", "env", "svr", "tc", null, null, null));
        assertTrue(elr.isStoredSuccessfully());
    }

    @Test
    public void testWhenValidAuthCreds() {
        EventLogClientConfig config = mock(EventLogClientConfig.class);
        when(configurator.getConfiguration(EventLogClientConfig.class)).thenReturn(config);
        when(config.contentType()).thenReturn("application/json");
        when(config.accept()).thenReturn("application/json");
        when(config.username()).thenReturn("user");
        when(config.password()).thenReturn("pass");
        Call<EventLogResponse> call = mock(Call.class);
        Response<EventLogResponse> response = Response.success(new EventLogResponse(true));
        when(eventLogTransport.postEventLog(anyMap(), any(EventLogRequest.class))).thenReturn(call);
        try {
            when(call.execute()).thenReturn(response);
        } catch (Exception ex) {
            fail("did not expect exception here");
        }
        EventLogResponse elr = eventLogClient.doPostEventLogRequest(new EventLogRequest("id", new Date(), "app", "env", "svr", "tc", null, null, null));
        assertTrue(elr.isStoredSuccessfully());
    }
}
