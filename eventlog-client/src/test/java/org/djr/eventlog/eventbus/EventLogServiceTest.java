package org.djr.eventlog.eventbus;

import org.djr.eventlog.EventLogClientController;
import org.djr.eventlog.rest.EventLogClient;
import org.djr.eventlog.rest.EventLogRequest;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(CdiRunner.class)
@AdditionalClasses({EventLogClientController.class, EventLogListener.class})
public class EventLogServiceTest {
    @Produces
    @Mock
    private EventLogClient eventLogClient;
    @Inject
    private EventLogService eventLogService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testEventLogListener() {
        EventLogRequest eventLogRequest = new EventLogRequest("123", ZonedDateTime.now().toInstant().toEpochMilli(),
                "app", "env", "server", "testEC", null,
                null, null);
        EventLogMessage eventLogMessage = new EventLogMessage(eventLogRequest);
        eventLogService.publishEventLogMessage(eventLogMessage);
        verify(eventLogClient, times(1)).doPostEventLogRequest(eventLogRequest);
    }
}
