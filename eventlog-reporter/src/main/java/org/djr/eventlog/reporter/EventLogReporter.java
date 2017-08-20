package org.djr.eventlog.reporter;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import com.codahale.metrics.health.HealthCheck;
import com.djr4488.metrics.MetricsRegistryBean;
import org.djr.eventlog.eventbus.EventLogMessage;
import org.djr.eventlog.eventbus.EventLogService;
import org.djr.eventlog.rest.EventLogRequest;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by djr4488 on 8/18/17.
 */
public class EventLogReporter extends ScheduledReporter {
    /**
     * Returns a new {@link Builder} for {@link EventLogReporter}.
     *
     * @param registry the registry to report
     * @return a {@link Builder} instance for a {@link EventLogReporter}
     */
    public static Builder forRegistry(MetricRegistry registry) {
        return new Builder(registry);
    }

    public static class Builder {
        private final MetricRegistry registry;
        private EventLogService eventLogService;
        private String trackingIdentifier;
        private TimeUnit rateUnit;
        private TimeUnit durationUnit;
        private MetricFilter filter;
        private String applicationName;
        private String environment;
        private String server;
        private Builder(MetricRegistry registry) {
            this.registry = registry;
            this.rateUnit = TimeUnit.SECONDS;
            this.durationUnit = TimeUnit.MILLISECONDS;
            this.filter = MetricFilter.ALL;
            this.trackingIdentifier = "eventlog-metrics-reporter";
            this.applicationName = null;
            this.environment = null;
            try {
                this.server = Inet4Address.getLocalHost().getHostName();
            } catch (UnknownHostException uhEx) {
                this.server = "unknown-server";
            }
        }

        /**
         * Log metrics to the given eventLogService.
         *
         * @param eventLogService an EventLogService {@link org.djr.eventlog.eventbus.EventLogService}
         * @return {@code this}
         */
        public Builder outputTo(EventLogService eventLogService) {
            this.eventLogService = eventLogService;
            return this;
        }

        /**
         * Convert rates to the given time unit.
         *
         * @param rateUnit a unit of time
         * @return {@code this}
         */
        public Builder convertRatesTo(TimeUnit rateUnit) {
            this.rateUnit = rateUnit;
            return this;
        }

        /**
         * Convert durations to the given time unit.
         *
         * @param durationUnit a unit of time
         * @return {@code this}
         */
        public Builder convertDurationsTo(TimeUnit durationUnit) {
            this.durationUnit = durationUnit;
            return this;
        }

        /**
         * Only report metrics which match the given filter.
         *
         * @param filter a {@link MetricFilter}
         * @return {@code this}
         */
        public Builder filter(MetricFilter filter) {
            this.filter = filter;
            return this;
        }

        public Builder trackingIdentifier(String trackingIdentifier) {
            this.trackingIdentifier = trackingIdentifier;
            return this;
        }

        public Builder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public Builder environment(String environment) {
            this.environment = environment;
            return this;
        }

        public Builder server(String server) {
            this.server = server;
            return this;
        }

        public Builder eventLogService(EventLogService eventLogService) {
            this.eventLogService = eventLogService;
            return this;
        }

        /**
         * Builds a {@link EventLogReporter} with the given properties.
         *
         * @return a {@link EventLogReporter}
         */
        public EventLogReporter build() {
            return new EventLogReporter(registry, rateUnit, durationUnit, filter, trackingIdentifier,
                    applicationName, environment, server, eventLogService);
        }
    }

    private final String trackingIdentifier;
    private final String applicationName;
    private final String environment;
    private final String server;
    private final EventLogService eventLogService;
    private static Logger log = LoggerFactory.getLogger(EventLogReporter.class);

    private EventLogReporter(MetricRegistry registry,
                             TimeUnit rateUnit,
                             TimeUnit durationUnit,
                             MetricFilter filter,
                             String trackingIdentifier,
                             String applicationName,
                             String environment,
                             String server,
                             EventLogService eventLogService) {
        super(registry, "event-log-filter", filter, rateUnit, durationUnit);
        this.trackingIdentifier = trackingIdentifier;
        this.applicationName = applicationName;
        this.environment = environment;
        this.server = server;
        this.eventLogService = eventLogService;
    }

    @Override
    public void report(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters, SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
        log.debug("report() kicking off event log report");
        String jvmUuid = UUID.randomUUID().toString();
        for (Map.Entry<String, Timer> entry : timers.entrySet()) {
            logTimer(entry.getKey(), entry.getValue(), jvmUuid);
        }
        for (Map.Entry<String, Meter> entry : meters.entrySet()) {
            logMeter(entry.getKey(), entry.getValue(), jvmUuid);
        }
        for (Map.Entry<String, Histogram> entry : histograms.entrySet()) {
            logHistogram(entry.getKey(), entry.getValue(), jvmUuid);
        }
        for (Map.Entry<String, Counter> entry : counters.entrySet()) {
            logCounter(entry.getKey(), entry.getValue(), jvmUuid);
        }
        try {
            MetricsRegistryBean metricsRegistryBean = MetricsRegistryBean.getBeanByNameOfClass("metricsRegistryBean", MetricsRegistryBean.class);
            SortedMap<String, HealthCheck.Result> healthChecks = metricsRegistryBean.getHealthCheckRegistry().runHealthChecks();
            for (Map.Entry<String, HealthCheck.Result> entry : healthChecks.entrySet()) {
                logHealthChecks(entry.getKey(), entry.getValue(), jvmUuid);
            }
        } catch (Exception ex) {
            EventLogRequest elr = new EventLogRequest(trackingIdentifier, System.currentTimeMillis(), applicationName, environment, server, "health", "metrics registry failed", ex.getMessage(), null);
            EventLogMessage elm = new EventLogMessage(elr);
            eventLogService.publishEventLogMessage(elm);
        }
        for (Map.Entry<String, Gauge> entry : gauges.entrySet()) {
            logGauge(entry.getKey(), entry.getValue(), jvmUuid);
        }
    }

    private void logTimer(String name, Timer timer, String jvmUuid) {
        log.debug("logTimer() name:{}, timer:{}", name, timer);
        final Snapshot snapshot = timer.getSnapshot();
        Map<String, String> dataPointMap = new HashMap<>();
        dataPointMap.put("timer-count", Long.toString(timer.getCount()));
        dataPointMap.put("timer-snapshot-min", Double.toString(convertDuration(snapshot.getMin())));
        dataPointMap.put("timer-snapshot-max", Double.toString(convertDuration(snapshot.getMax())));
        dataPointMap.put("timer-snapshot-mean", Double.toString(convertDuration(snapshot.getMean())));
        dataPointMap.put("timer-snapshot-stddev", Double.toString(convertDuration(snapshot.getStdDev())));
        dataPointMap.put("timer-snapshot-median", Double.toString(convertDuration(snapshot.getMedian())));
        dataPointMap.put("timer-snapshot-75thPercentile", Double.toString(convertDuration(snapshot.get75thPercentile())));
        dataPointMap.put("timer-snapshot-95thPercentile", Double.toString(convertDuration(snapshot.get95thPercentile())));
        dataPointMap.put("timer-snapshot-98thPercentile", Double.toString(convertDuration(snapshot.get98thPercentile())));
        dataPointMap.put("timer-snapshot-99thPercentile", Double.toString(convertDuration(snapshot.get99thPercentile())));
        dataPointMap.put("timer-snapshot-999thPercentile", Double.toString(convertDuration(snapshot.get999thPercentile())));
        dataPointMap.put("timer-mean-rate", Double.toString(timer.getMeanRate()));
        dataPointMap.put("timer-one-minute-rate", Double.toString(timer.getOneMinuteRate()));
        dataPointMap.put("timer-five-minute-rate", Double.toString(timer.getFiveMinuteRate()));
        dataPointMap.put("timer-fifteen-minute-rate", Double.toString(timer.getFifteenMinuteRate()));
        dataPointMap.put("timer-rate-unit", getRateUnit());
        dataPointMap.put("timer-duration-unit", getDurationUnit());
        EventLogRequest elr = new EventLogRequest(isValidTrackingIdentifier() ? trackingIdentifier : jvmUuid,
                DateTime.now().getMillis(), applicationName, environment, server, name, null, null, dataPointMap);
        EventLogMessage elm = new EventLogMessage(elr);
        eventLogService.publishEventLogMessage(elm);
    }

    private void logHealthChecks(String name, HealthCheck.Result result, String jvmUuid) {
        log.debug("logHealthCheck() name:{}, result:{}", name, result);
        Map<String, String> dataPointMap = new HashMap<>();
        dataPointMap.put("health-result-is-healthy", Boolean.toString(result.isHealthy()));
        String busErrorCode = null;
        String sysErrorCode = null;
        if (!result.isHealthy()) {
            dataPointMap.put("health-result-error", healthCheckErrorToString(result));
            busErrorCode = "Health Check";
            sysErrorCode = result.getMessage();
        }
        EventLogRequest elr = new EventLogRequest(isValidTrackingIdentifier() ? trackingIdentifier : jvmUuid, DateTime.now().getMillis(), applicationName,
                environment, server, name, busErrorCode, sysErrorCode, dataPointMap);
        EventLogMessage elm = new EventLogMessage(elr);
        eventLogService.publishEventLogMessage(elm);
    }

    private String healthCheckErrorToString(HealthCheck.Result result) {
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement stackTraceElement : result.getError().getStackTrace()) {
            stringBuilder.append(stackTraceElement.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

    private void logMeter(String name, Meter meter, String jvmUuid) {
        Map<String, String> dataPointMap = new HashMap<>();
        dataPointMap.put("meter-count", Long.toString(meter.getCount()));
        dataPointMap.put("meter-mean-rate", Double.toString(meter.getMeanRate()));
        dataPointMap.put("meter-one-minute-rate", Double.toString(meter.getOneMinuteRate()));
        dataPointMap.put("meter-five-minute-rate", Double.toString(meter.getFiveMinuteRate()));
        dataPointMap.put("meter-fifteen-minute-rate", Double.toString(meter.getFifteenMinuteRate()));
        dataPointMap.put("meter-rate-unit", getRateUnit());
        EventLogRequest elr = new EventLogRequest(isValidTrackingIdentifier() ? trackingIdentifier : jvmUuid,
                DateTime.now().getMillis(), applicationName, environment, server, name, null, null, dataPointMap);
        EventLogMessage elm = new EventLogMessage(elr);
        eventLogService.publishEventLogMessage(elm);
    }

    private void logHistogram(String name, Histogram histogram, String jvmUuid) {
        final Snapshot snapshot = histogram.getSnapshot();
        Map<String, String> dataPointMap = new HashMap<>();
        dataPointMap.put("histogram-count", Long.toString(histogram.getCount()));
        dataPointMap.put("histogram-min", Double.toString(snapshot.getMin()));
        dataPointMap.put("histogram-max", Double.toString(snapshot.getMax()));
        dataPointMap.put("histogram-mean", Double.toString(snapshot.getMean()));
        dataPointMap.put("histogram-stddev", Double.toString(snapshot.getStdDev()));
        dataPointMap.put("histogram-Median", Double.toString(snapshot.getMedian()));
        dataPointMap.put("histogram-75th-percentile", Double.toString(snapshot.get75thPercentile()));
        dataPointMap.put("histogram-95th-percentile", Double.toString(snapshot.get95thPercentile()));
        dataPointMap.put("histogram-98th-percentile", Double.toString(snapshot.get98thPercentile()));
        dataPointMap.put("histogram-99th-percentile", Double.toString(snapshot.get99thPercentile()));
        dataPointMap.put("histogram-999th-percentile", Double.toString(snapshot.get999thPercentile()));
        EventLogRequest elr = new EventLogRequest(isValidTrackingIdentifier() ? trackingIdentifier : jvmUuid,
                DateTime.now().getMillis(), applicationName, environment, server, name, null, null, dataPointMap);
        EventLogMessage elm = new EventLogMessage(elr);
        eventLogService.publishEventLogMessage(elm);
    }

    private void logCounter(String name, Counter counter, String jvmUuid) {
        Map<String, String> dataPointMap = new HashMap<>();
        dataPointMap.put("counter-count", Long.toString(counter.getCount()));
        EventLogRequest elr = new EventLogRequest(isValidTrackingIdentifier() ? trackingIdentifier : jvmUuid,
                DateTime.now().getMillis(), applicationName, environment, server, name, null, null, dataPointMap);
        EventLogMessage elm = new EventLogMessage(elr);
        eventLogService.publishEventLogMessage(elm);
    }

    private void logGauge(String name, Gauge gauge, String jvmUuid) {
        Map<String, String> dataPointMap = new HashMap<>();
        dataPointMap.put(name, gauge.getValue().toString());
        EventLogRequest elr = new EventLogRequest(isValidTrackingIdentifier() ? trackingIdentifier : jvmUuid, DateTime.now().getMillis(), applicationName,
                environment, server, name, null, null, dataPointMap);
        EventLogMessage elm = new EventLogMessage(elr);
        eventLogService.publishEventLogMessage(elm);
    }

    private boolean isValidTrackingIdentifier() {
        return null != trackingIdentifier && !(0 < trackingIdentifier.length());
    }
}
