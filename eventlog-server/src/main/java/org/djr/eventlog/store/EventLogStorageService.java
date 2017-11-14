package org.djr.eventlog.store;

import org.djr.eventlog.elasticsearch.cdi.ElasticSearch;
import org.djr.eventlog.elasticsearch.cdi.ElasticSearchConfig;
import org.djr.eventlog.rest.EventLogRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
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
        DateTime dt = DateTime.now().withMillis(eventLogRequest.getEventOccurredAt());
        EventLog eventLog = new EventLog(dt.toDate(), eventLogRequest.getApplicationName(),
                eventLogRequest.getEnvironment(), eventLogRequest.getServer(), eventLogRequest.getEventCode(),
                eventLogRequest.getBusinessErrorCode(), eventLogRequest.getSystemErrorCode(),
                eventLogRequest.getTrackingIdentifier(), eventLogRequest.isAlertOnError(), null);
        eventLog.setCreatedAt(new Date());
        eventLog.setLastUpdatedAt(new Date());
        if (null != eventLogRequest.getDataPoints() && !eventLogRequest.getDataPoints().isEmpty()) {
            dataPoints = getDataPoints(eventLogRequest.getDataPoints(), eventLog);
            eventLog.setDataPoint(dataPoints);
        }
        return eventLog;
    }

    private List<DataPoint> getDataPoints(Map<String, String> dataPointMap, EventLog eventLog) {
        List<DataPoint> dataPoints = new ArrayList<>(dataPointMap.size());
        Date now = new Date();
        for (String dataPointName : dataPointMap.keySet()) {
            DataPoint dataPoint = new DataPoint(dataPointName, dataPointMap.get(dataPointName), eventLog);
            dataPoint.setCreatedAt(now);
            dataPoint.setLastUpdatedAt(now);
            dataPoints.add(dataPoint);
        }
        return dataPoints;
    }
}
