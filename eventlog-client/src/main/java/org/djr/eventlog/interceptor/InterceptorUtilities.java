package org.djr.eventlog.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;

public class InterceptorUtilities {
    private static final Logger log = LoggerFactory.getLogger(InterceptorUtilities.class);
    public static String getServerInfo() {
        String appName = null;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            appName = inetAddress.getHostName() + " - " + inetAddress.getHostAddress();
        } catch (UnknownHostException uhEx) {
            log.debug("getServerInfo() unable to get server info for naming");
        }
        return appName;
    }

    public static boolean isFieldAJavaCollectionOrArray(Field field) {
        return Collections.class.isAssignableFrom(field.getType()) || field.getType().isArray();
    }

    public  static String getErrorType(Exception ex, Class<? extends Exception>[] businessExceptions) {
        String exceptionType = "S";
        for (Class<? extends Exception> businessException : businessExceptions) {
            if (ex.getClass().getName().equals(businessException.getName())) {
                exceptionType = "B";
                break;
            }
        }
        return exceptionType;
    }
}
