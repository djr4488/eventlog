package org.djr.eventlog.interceptor;


import org.djr.eventlog.annotations.EventLogParameter;
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

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;

@RunWith(CdiRunner.class)
@AdditionalClasses({EventLogInterceptor.class})
public class EventLogInterceptorTest {
    @Produces
    @Mock
    private EventLogService eventLogService;
    @Inject
    private Intercepted intercepted;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
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
    }
}
