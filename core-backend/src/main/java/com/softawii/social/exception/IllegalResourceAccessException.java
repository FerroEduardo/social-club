package com.softawii.social.exception;

public class IllegalResourceAccessException extends Exception {
    public IllegalResourceAccessException() {
    }

    public IllegalResourceAccessException(String message) {
        super(message);
    }

    public IllegalResourceAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalResourceAccessException(Throwable cause) {
        super(cause);
    }

    public IllegalResourceAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
