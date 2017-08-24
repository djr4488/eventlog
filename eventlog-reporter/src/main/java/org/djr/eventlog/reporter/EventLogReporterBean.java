package org.djr.eventlog.reporter;

import com.codahale.metrics.MetricRegistry;
import com.djr4488.metrics.reporters.ReporterBean;
import org.djr.eventlog.Configurator;
import org.djr.eventlog.eventbus.EventLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * Created by djr4488 on 8/18/17.
 */
@ApplicationScoped
@Named("eventLogReporterBean")
public class EventLogReporterBean implements ReporterBean {
    private static Logger log = LoggerFactory.getLogger(EventLogReporterBean.class);
    @Inject
    private EventLogService eventLogService;
    @Inject
    private Configurator configurator;
    private EventLogReporter eventLogReporter;
    private EventLogReporterBeanConfig config;
    private String SERVER;
    @Resource(lookup="java:app/AppName")
    private String resourceAppName;

    @PostConstruct
    public void postConstruct() {
        config = configurator.getConfiguration(EventLogReporterBeanConfig.class);
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            SERVER = localHost.getHostName() + " - " + localHost.getHostAddress();
        } catch (UnknownHostException uhEx) {
            SERVER = "unknown-host-name-and-address";
        }
    }

    @Override
    public void initialize(MetricRegistry metricRegistry) {
        log.debug("initialize() initializing eventLogReporter with metricRegistry:{}", metricRegistry);
        eventLogReporter = EventLogReporter.forRegistry(metricRegistry)
                .eventLogService(eventLogService)
                .applicationName(resourceAppName)
                .outputTo(eventLogService)
                .server(SERVER)
                .build();
        startReporter();
    }

    @Override
    public void stopReporter() {
        eventLogReporter.stop();
    }

    @Override
    public void startReporter() {
        log.debug("startReporter() starting eventLogReporter");
        Integer period = config.period();
        TimeUnit timeUnit = TimeUnit.valueOf(config.timeUnit());
        eventLogReporter.start(period, timeUnit);
    }

    @Override
    public boolean isReporterBeanEnabled() {
        return true;
    }
}
