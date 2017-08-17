package org.djr.eventlog.store;

import org.djr.eventlog.rest.EventLogRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class EventLogStorageService {
    private static Logger log = LoggerFactory.getLogger(EventLogStorageService.class);
    @PersistenceContext(unitName = "eventlog")
    private EntityManager em;

    public void storeEventLog(EventLogRequest eventLogRequest) {
        log.debug("storeEventLog() eventLogRequest:{}", eventLogRequest);
        EventLog eventLog = convertEventLogRequestToEventLog(eventLogRequest);
        em.persist(eventLog);
    }

    private EventLog convertEventLogRequestToEventLog(EventLogRequest eventLogRequest) {
        List<DataPoint> dataPoints = null;
        EventLog eventLog = new EventLog(eventLogRequest.getEventOccurredAt(), eventLogRequest.getApplicationName(),
                eventLogRequest.getEnvironment(), eventLogRequest.getServer(), eventLogRequest.getEventCode(),
                eventLogRequest.getBusinessErrorCode(), eventLogRequest.getSystemErrorCode(),
                eventLogRequest.getTrackingIdentifier(), null);
        if (null != eventLogRequest.getDataPoints() && !eventLogRequest.getDataPoints().isEmpty()) {
            dataPoints = getDataPoints(eventLogRequest.getDataPoints(), eventLog);
            eventLog.setDataPoint(dataPoints);
        }
        return eventLog;
    }

    private List<DataPoint> getDataPoints(Map<String, String> dataPointMap, EventLog eventLog) {
        List<DataPoint> dataPoints = new ArrayList<>(dataPointMap.size());
        for (String dataPointName : dataPointMap.keySet()) {
            dataPoints.add(new DataPoint(dataPointName, dataPointMap.get(dataPointName), eventLog));
        }
        return dataPoints;
    }
}
