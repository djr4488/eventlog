package webfilter;

import org.djr.eventlog.EventLogConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * Created by djr4488 on 8/20/17.
 */
@WebFilter(filterName = "eventlog-web-filter", urlPatterns = "/*")
public class EventLogWebFilter implements Filter {
    private static Logger log = LoggerFactory.getLogger(EventLogWebFilter.class);
    @Resource(lookup="java:app/AppName")
    private String resourceAppName;

    @Override
    public void init(FilterConfig filterConfig)
    throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
    throws IOException, ServletException {
        log.debug("doFilter() capturing params for MDC");
        String eventLogTrackingId = UUID.randomUUID().toString();
        MDC.put(EventLogConstants.eventLogTrackingIdKey, eventLogTrackingId);
        MDC.put(EventLogConstants.eventLogApplicationNameKey, resourceAppName);
        MDC.put(EventLogConstants.eventLogServerKey, getServerInfo());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private String getServerInfo() {
        String appName = null;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            appName = inetAddress.getHostName() + " - " + inetAddress.getHostAddress();
        } catch (UnknownHostException uhEx) {
            log.debug("getServerInfo() unable to get server info for naming");
        }
        return appName;
    }
}
