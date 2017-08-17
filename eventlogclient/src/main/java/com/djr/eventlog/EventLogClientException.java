package com.djr.eventlog;

public class EventLogClientException extends RuntimeException {
    public EventLogClientException() {
        super();
    }

    public EventLogClientException(String message) {
        super(message);
    }

    public EventLogClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventLogClientException(Throwable cause) {
        super(cause);
    }

    protected EventLogClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
