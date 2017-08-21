package org.djr.eventlog.interceptor;


import org.djr.eventlog.Configurator;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(CdiRunner.class)
@AdditionalClasses({EventLogInterceptor.class})
public class EventLogInterceptorTest {
    private static Logger log = LoggerFactory.getLogger(EventLogInterceptor.class);
    @Produces
    @Mock
    private EventLogService eventLogService;
    @Produces
    @Mock
    private Configurator configurator;
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
        assertEquals(3, elr.getDataPoints().size());
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
        assertEquals(2, elr.getDataPoints().size());
        assertTrue(elr.getDataPoints().values().contains("interceptingStructTypes"));
        assertEquals("InterceptedStruct[someValue=something,someIntValue=3,someBooleanValue=false]", elr.getDataPoints().get("org.djr.eventlog.interceptor.InterceptedStruct"));
        assertEquals("ABC123", elr.getTrackingIdentifier());
        assertEquals("eventLog", elr.getApplicationName());
        assertEquals("local_machine", elr.getServer());
    }
}
