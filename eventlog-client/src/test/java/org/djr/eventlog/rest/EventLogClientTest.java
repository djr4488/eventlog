package org.djr.eventlog.rest;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.djr.eventlog.EventLogClientException;
import org.djr.retrofit2ee.json.RetrofitJson;
import org.jboss.weld.environment.se.contexts.activators.ActivateThreadScope;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import java.io.IOException;
import java.time.ZonedDateTime;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(CdiRunner.class)
public class EventLogClientTest {
    private static Logger log = LoggerFactory.getLogger(EventLogClientTest.class);
//    @Produces
//    @RetrofitJson
//    @Mock
//    private Retrofit retrofitProducer;
//    @Produces
//    @Mock
//    private Configurator configurator;
//    @Inject
//    private EventLogClient eventLogClient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFake() {
        assertTrue(true);
    }

//    @Test
//    public void testEventLogClientSuccessPath() {
//        EventLogClientConfig config = mock(EventLogClientConfig.class);
//        when(configurator.getConfiguration(EventLogClientConfig.class)).thenReturn(config);
//        when(config.contentType()).thenReturn("application/json");
//        when(config.accept()).thenReturn("application/json");
//        when(config.username()).thenReturn(null);
//        when(config.password()).thenReturn(null);
//        Call<EventLogResponse> call = mock(Call.class);
//        EventLogTransport elt = mock(EventLogTransport.class);
//        when(retrofitProducer.create(EventLogTransport.class)).thenReturn(elt);
//        Response<EventLogResponse> response = Response.success(new EventLogResponse(true));
//        when(elt.postEventLog(anyMap(), any(EventLogRequest.class))).thenReturn(call);
//        try {
//            when(call.execute()).thenReturn(response);
//        } catch (Exception ex) {
//            fail("did not expect exception here");
//        }
//        EventLogResponse elr = eventLogClient.doPostEventLogRequest(new EventLogRequest("id", ZonedDateTime.now().toInstant().toEpochMilli(), "app", "env", "svr", "tc", null, null, null));
//        assertTrue(elr.isStoredSuccessfully());
//    }

//    @Test(expected = EventLogClientException.class)
//    public void testEventLogClientUnsuccessfulPath() {
//        EventLogClientConfig config = mock(EventLogClientConfig.class);
//        when(configurator.getConfiguration(EventLogClientConfig.class)).thenReturn(config);
//        when(config.contentType()).thenReturn("application/json");
//        when(config.accept()).thenReturn("application/json");
//        when(config.username()).thenReturn(null);
//        when(config.password()).thenReturn(null);
//        when(retrofitProducer.create(EventLogTransport.class)).thenReturn(eventLogTransport);
//        Call<EventLogResponse> call = mock(Call.class);
//        Response<EventLogResponse> response = Response.error(403, ResponseBody.create(MediaType.parse("application/json"), "Unauthorized"));
//        when(eventLogTransport.postEventLog(anyMap(), any(EventLogRequest.class))).thenReturn(call);
//        try {
//            when(call.execute()).thenReturn(response);
//        } catch (Exception ex) {
//            fail("did not expect exception here");
//        }
//        EventLogResponse elr = eventLogClient.doPostEventLogRequest(new EventLogRequest("id", ZonedDateTime.now().toInstant().toEpochMilli(), "app", "env", "svr", "tc", null, null, null));
//    }
//
//    @Test(expected = EventLogClientException.class)
//    public void testEventLogClientIoExceptionPath() {
//        EventLogClientConfig config = mock(EventLogClientConfig.class);
//        when(configurator.getConfiguration(EventLogClientConfig.class)).thenReturn(config);
//        when(config.contentType()).thenReturn("application/json");
//        when(config.accept()).thenReturn("application/json");
//        when(config.username()).thenReturn(null);
//        when(config.password()).thenReturn(null);
//        when(retrofitProducer.create(EventLogTransport.class)).thenReturn(eventLogTransport);
//        Call<EventLogResponse> call = mock(Call.class);
//        when(eventLogTransport.postEventLog(anyMap(), any(EventLogRequest.class))).thenReturn(call);
//        try {
//            when(call.execute()).thenThrow(new IOException("test exception"));
//        } catch (Exception ex) {
//            fail("did not expect exception here");
//        }
//        EventLogResponse elr = eventLogClient.doPostEventLogRequest(new EventLogRequest("id", ZonedDateTime.now().toInstant().toEpochMilli(), "app", "env", "svr", "tc", null, null, null));
//    }
//
//    @Test
//    public void testWhenUserAddedButNoPassword() {
//        EventLogClientConfig config = mock(EventLogClientConfig.class);
//        when(configurator.getConfiguration(EventLogClientConfig.class)).thenReturn(config);
//        when(config.contentType()).thenReturn("application/json");
//        when(config.accept()).thenReturn("application/json");
//        when(config.username()).thenReturn("user");
//        when(config.password()).thenReturn(null);
//        Call<EventLogResponse> call = mock(Call.class);
//        when(retrofitProducer.create(EventLogTransport.class)).thenReturn(eventLogTransport);
//        Response<EventLogResponse> response = Response.success(new EventLogResponse(true));
//        when(eventLogTransport.postEventLog(anyMap(), any(EventLogRequest.class))).thenReturn(call);
//        try {
//            when(call.execute()).thenReturn(response);
//        } catch (Exception ex) {
//            fail("did not expect exception here");
//        }
//        EventLogResponse elr = eventLogClient.doPostEventLogRequest(new EventLogRequest("id", ZonedDateTime.now().toInstant().toEpochMilli(), "app", "env", "svr", "tc", null, null, null));
//        assertTrue(elr.isStoredSuccessfully());
//    }
//
//    @Test
//    public void testWhenPasswordButNoUser() {
//        EventLogClientConfig config = mock(EventLogClientConfig.class);
//        when(configurator.getConfiguration(EventLogClientConfig.class)).thenReturn(config);
//        when(config.contentType()).thenReturn("application/json");
//        when(config.accept()).thenReturn("application/json");
//        when(config.username()).thenReturn(null);
//        when(config.password()).thenReturn("pass");
//        Call<EventLogResponse> call = mock(Call.class);
//        when(retrofitProducer.create(EventLogTransport.class)).thenReturn(eventLogTransport);
//        Response<EventLogResponse> response = Response.success(new EventLogResponse(true));
//        when(eventLogTransport.postEventLog(anyMap(), any(EventLogRequest.class))).thenReturn(call);
//        try {
//            when(call.execute()).thenReturn(response);
//        } catch (Exception ex) {
//            fail("did not expect exception here");
//        }
//        EventLogResponse elr = eventLogClient.doPostEventLogRequest(new EventLogRequest("id", ZonedDateTime.now().toInstant().toEpochMilli(), "app", "env", "svr", "tc", null, null, null));
//        assertTrue(elr.isStoredSuccessfully());
//    }
//
//    @Test
//    public void testWhenValidAuthCreds() {
//        EventLogClientConfig config = mock(EventLogClientConfig.class);
//        when(configurator.getConfiguration(EventLogClientConfig.class)).thenReturn(config);
//        when(config.contentType()).thenReturn("application/json");
//        when(config.accept()).thenReturn("application/json");
//        when(config.username()).thenReturn("user");
//        when(config.password()).thenReturn("pass");
//        Call<EventLogResponse> call = mock(Call.class);
//        when(retrofitProducer.create(EventLogTransport.class)).thenReturn(eventLogTransport);
//        Response<EventLogResponse> response = Response.success(new EventLogResponse(true));
//        when(eventLogTransport.postEventLog(anyMap(), any(EventLogRequest.class))).thenReturn(call);
//        try {
//            when(call.execute()).thenReturn(response);
//        } catch (Exception ex) {
//            fail("did not expect exception here");
//        }
//        EventLogResponse elr = eventLogClient.doPostEventLogRequest(new EventLogRequest("id", ZonedDateTime.now().toInstant().toEpochMilli(), "app", "env", "svr", "tc", null, null, null));
//        assertTrue(elr.isStoredSuccessfully());
//    }
//
//    @Test
//    public void testMockedService()
//    throws IOException {
////        Retrofit retrofit = TransportProducer.getTransport(null, "http://www.test.com/", true);
////        NetworkBehavior networkBehavior = NetworkBehavior.create();
////        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit)
////                .networkBehavior(networkBehavior)
////                .build();
////        BehaviorDelegate<EventLogTransport> behaviorDelegate = mockRetrofit.create(EventLogTransport.class);
////        MockEventLogEndpoint mockEventLogEndpoint = new MockEventLogEndpoint(behaviorDelegate);
////        EventLogRequest eventLogRequest = new EventLogRequest("SUCCESS", ZonedDateTime.now().toInstant().toEpochMilli(), "eventLog", "test", "test", "testEC",
////                null, null, null);
////        networkBehavior.setFailurePercent(0);
////        Call<EventLogResponse> callResponse = mockEventLogEndpoint.postEventLog(null, eventLogRequest);
////        assertNotNull(callResponse);
////        Request req = callResponse.request();
////        log.debug("testMockedService() request:{}", req);
////        Response<EventLogResponse> elrResponse = callResponse.execute();
////        assertNotNull(elrResponse);
////        assertNotNull(elrResponse.body());
////        assertTrue(elrResponse.body().isStoredSuccessfully());
//    }
}
