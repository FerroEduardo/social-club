package com.softawii.gateway.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.Arrays;

public class RecaptchaResponse {

    private boolean       success;
    @JsonProperty("challenge_ts")
    private ZonedDateTime timestamp;
    private String        hostname;
    @JsonProperty("error-codes")
    private String[]      errorCodes;

    public RecaptchaResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String[] getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(String[] errorCodes) {
        this.errorCodes = errorCodes;
    }

    @Override
    public String toString() {
        return "RecaptchaResponse{" +
                "success=" + success +
                ", timestamp=" + timestamp +
                ", hostname='" + hostname + '\'' +
                ", errorCodes=" + Arrays.toString(errorCodes) +
                '}';
    }
}
