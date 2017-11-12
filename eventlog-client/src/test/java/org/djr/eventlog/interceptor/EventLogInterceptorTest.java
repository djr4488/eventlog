package org.djr.eventlog.interceptor;

import org.djr.eventlog.EventController;
import org.djr.eventlog.EventLogConstants;
import org.djr.eventlog.eventbus.EventLogMessage;
import org.djr.eventlog.eventbus.EventLogService;
import org.djr.eventlog.rest.EventLogRequest;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;

@RunWith(CdiRunner.class)
@AdditionalClasses({EventLogInterceptor.class})
public class EventLogInterceptorTest {
    private static Logger log = LoggerFactory.getLogger(EventLogInterceptorTest.class);
    @Produces
    @Mock
    private EventController eventLogService;
    @Inject
    private Intercepted intercepted;

    @Before
    public void setup() {
        log.debug("setup() setting up mocks and MDC values");
        MockitoAnnotations.initMocks(this);
        MDC.put(EventLogConstants.eventLogTrackingIdKey, "ABC123");
        MDC.put(EventLogConstants.eventLogApplicationNameKey, "eventLog");
        MDC.put(EventLogConstants.eventLogServerKey, "local_machine");
    }

    @Test
    public void testInterceptor() {
        ArgumentCaptor<EventLogMessage> elmArgumentCaptor = ArgumentCaptor.forClass(EventLogMessage.class);
        doNothing().when(eventLogService).publishEventLogMessage(elmArgumentCaptor.capture());
        intercepted.interceptingMe("no", "yes", "no", 3);
        EventLogRequest elr = elmArgumentCaptor.getValue().getEventLogRequest();
        assertEquals(5, elr.getDataPoints().size());
        assertTrue(elr.getDataPoints().values().contains("interceptingMe"));
        assertTrue(elr.getDataPoints().values().contains("yes"));
        assertTrue(elr.getDataPoints().values().contains("3"));
        assertFalse(elr.getDataPoints().values().contains("no"));
        assertEquals("ABC123", elr.getTrackingIdentifier());
        assertEquals("eventLog", elr.getApplicationName());
        assertEquals("local_machine", elr.getServer());
    }

    @Test
    public void testInterceptorWithStruct() {
        ArgumentCaptor<EventLogMessage> elmArgumentCaptor = ArgumentCaptor.forClass(EventLogMessage.class);
        doNothing().when(eventLogService).publishEventLogMessage(elmArgumentCaptor.capture());
        intercepted.interceptingStructTypes(new InterceptedStruct("something", 3, false));
        EventLogRequest elr = elmArgumentCaptor.getValue().getEventLogRequest();
        assertEquals(6, elr.getDataPoints().size());
        assertTrue(elr.getDataPoints().values().contains("interceptingStructTypes"));
        //assertEquals("InterceptedStruct[someValue=something,someIntValue=3,someBooleanValue=false]", elr.getDataPoints().get("org.djr.eventlog.interceptor.InterceptedStruct"));
        assertEquals("ABC123", elr.getTrackingIdentifier());
        assertEquals("eventLog", elr.getApplicationName());
        assertEquals("local_machine", elr.getServer());
        assertEquals("*", elr.getDataPoints().get("somethingOfValue"));
        assertEquals("3", elr.getDataPoints().get("someIntValue"));
        assertEquals("false", elr.getDataPoints().get("someBooleanValue"));
        assertEquals("is null", elr.getDataPoints().get("Method Intercept Return"));
    }

    @Test
    public void testInterceptorWithStructAndReturn() {
        ArgumentCaptor<EventLogMessage> elmArgumentCaptor = ArgumentCaptor.forClass(EventLogMessage.class);
        doNothing().when(eventLogService).publishEventLogMessage(elmArgumentCaptor.capture());
        intercepted.interceptingStructWithReturn(new InterceptedStruct("something", 3, false));
        EventLogRequest elr = elmArgumentCaptor.getValue().getEventLogRequest();
        assertEquals(7, elr.getDataPoints().size());
        assertEquals("interceptingStructWithReturn", elr.getDataPoints().get("methodName"));
        //assertEquals("InterceptedStruct[someValue=something,someIntValue=3,someBooleanValue=false]", elr.getDataPoints().get("org.djr.eventlog.interceptor.InterceptedStruct"));
        assertEquals("ABC123", elr.getTrackingIdentifier());
        assertEquals("eventLog", elr.getApplicationName());
        assertEquals("local_machine", elr.getServer());
        assertEquals("*", elr.getDataPoints().get("somethingOfValue"));
        assertEquals("3", elr.getDataPoints().get("someIntValue"));
        assertEquals("false", elr.getDataPoints().get("someBooleanValue"));
        assertEquals("*", elr.getDataPoints().get("someReturnMasked"));
        assertEquals("somethingReturned", elr.getDataPoints().get("someReturnValue"));
    }

    @Test
    public void testInterceptorWhenInterceptedException() {
        ArgumentCaptor<EventLogMessage> elmArgumentCaptor = ArgumentCaptor.forClass(EventLogMessage.class);
        doNothing().when(eventLogService).publishEventLogMessage(elmArgumentCaptor.capture());
        try {
            intercepted.interceptingStructWithException(new InterceptedStruct("something", 3, false));
        } catch (Exception ex) {
            assertTrue(ex.getMessage().equals("test message"));
        }
        EventLogRequest elr = elmArgumentCaptor.getValue().getEventLogRequest();
        assertEquals(8, elr.getDataPoints().size());
        assertTrue(elr.getDataPoints().values().contains("interceptingStructWithException"));
        //assertEquals("InterceptedStruct[someValue=something,someIntValue=3,someBooleanValue=false]", elr.getDataPoints().get("org.djr.eventlog.interceptor.InterceptedStruct"));
        assertEquals("ABC123", elr.getTrackingIdentifier());
        assertEquals("eventLog", elr.getApplicationName());
        assertEquals("local_machine", elr.getServer());
        assertEquals("*", elr.getDataPoints().get("somethingOfValue"));
        assertEquals("3", elr.getDataPoints().get("someIntValue"));
        assertEquals("false", elr.getDataPoints().get("someBooleanValue"));
        assertEquals("java.lang.NullPointerException", elr.getDataPoints().get("Exception Type"));
        assertEquals("test message", elr.getDataPoints().get("Exception Message"));
    }
}
