package com.djr.eventlog;

public class EventLogException extends RuntimeException {
    public EventLogException() {
        super();
    }

    public EventLogException(String message) {
        super(message);
    }

    public EventLogException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventLogException(Throwable cause) {
        super(cause);
    }

    protected EventLogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
