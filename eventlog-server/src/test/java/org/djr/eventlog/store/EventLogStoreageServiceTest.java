package org.djr.eventlog.store;

import org.djr.eventlog.rest.EventLogRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;

/**
 * Created by djr4488 on 8/21/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class EventLogStoreageServiceTest {
    private static Logger log = LoggerFactory.getLogger(EventLogStoreageServiceTest.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPlaceHolder() {
        assertTrue(true);
    }

//    @Test
//    public void testStoreEventLog() {
//        ArgumentCaptor<EventLog> elCaptor = ArgumentCaptor.forClass(EventLog.class);
//        doNothing().when(em).persist(elCaptor.capture());
//        eventLogJpaService.storeEventLog(createEventLogRequest());
//        EventLog el = elCaptor.getValue();
//        assertNotNull(el);
//        assertEquals("Test_123", el.getTrackingIdentifier());
//        assertNotNull(el.getDataPoint());
//        assertTrue(!el.getDataPoint().isEmpty());
//        assertEquals("data-point-value", el.getDataPoint().get(0).getDataPointValue());
//        assertEquals("test-data-point", el.getDataPoint().get(0).getDataPointName());
//    }

    private EventLogRequest createEventLogRequest() {
        Map<String, String> dataPointMap = new HashMap<>();
        dataPointMap.put("test-data-point", "data-point-value");
        return new EventLogRequest("123", 123L, "app", "env",
                "svr", "evtCd", "errCd", "evtType", false, false,
                123L, 124L, null);
    }
}


