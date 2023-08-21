package com.softawii.social.exception;

public class FailedToCreateImageException extends Exception {
    public FailedToCreateImageException() {
    }

    public FailedToCreateImageException(String message) {
        super(message);
    }

    public FailedToCreateImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedToCreateImageException(Throwable cause) {
        super(cause);
    }

    public FailedToCreateImageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
