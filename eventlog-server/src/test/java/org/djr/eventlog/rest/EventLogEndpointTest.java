package org.djr.eventlog.rest;

import org.djr.eventlog.EventLogController;
import org.djr.eventlog.EventLogException;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.Date;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by djr4488 on 8/21/17.
 */
@RunWith(CdiRunner.class)
public class EventLogEndpointTest {
    private static Logger log = LoggerFactory.getLogger(EventLogEndpoint.class);
    @Produces
    @Mock
    private EventLogController eventLogController;
    @Inject
    private EventLogEndpoint eventLogEndpoint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHappyPath() {
        log.info("testHappyPath()");
        EventLogRequest elReq = generateFakeEventLogRequest();
        doNothing().when(eventLogController).doHandleEventLogRequest(elReq);
        EventLogResponse elr = eventLogEndpoint.postEventLogRequest(elReq);
        verify(eventLogController, times(1)).doHandleEventLogRequest(elReq);
        assertTrue(elr.isStoredSuccessfully());
    }

    @Test(expected = EventLogException.class)
    public void testUnhappyPath() {
        log.info("testUnhappyPath()");
        EventLogRequest elReq = generateFakeEventLogRequest();
        doThrow(new EventLogException("test")).when(eventLogController).doHandleEventLogRequest(elReq);
        EventLogResponse elr = eventLogEndpoint.postEventLogRequest(elReq);
        verify(eventLogController, never()).doHandleEventLogRequest(elReq);
    }

    private EventLogRequest generateFakeEventLogRequest() {
        return new EventLogRequest("123", 123L, "app", "env",
                "svr", "evtCd", "errCd", "evtType", false,
                123L, 124L, null);
    }
}
