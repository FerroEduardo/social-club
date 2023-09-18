package com.softawii.gateway.exception;

import com.softawii.gateway.entity.RecaptchaResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class RecaptchaFailedException extends RuntimeException {
    private RecaptchaResponse response;

    public RecaptchaFailedException(String message) {
        super(message);
        this.response = null;
    }

    public RecaptchaFailedException(String message, RecaptchaResponse response) {
        super(message);
        this.response = response;
    }

    public RecaptchaResponse getResponse() {
        return response;
    }

    public Mono<ServerResponse> getServerResponse() {
        Map<String, String> body = new HashMap<>(2);
        body.put("message", "Failed to validate reCAPTCHA");
        body.put("code", "recaptcha-failed");

        return ServerResponse.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body));
    }
}
