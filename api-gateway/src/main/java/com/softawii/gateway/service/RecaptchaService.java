package com.softawii.gateway.service;

import com.softawii.gateway.entity.RecaptchaResponse;
import com.softawii.gateway.exception.RecaptchaFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

@Component
public class RecaptchaService {

    private static final String VALIDATE_URL = "https://www.google.com/recaptcha/api/siteverify";
    private final Logger logger = LoggerFactory.getLogger(RecaptchaService.class);
    @Value("${softawii.recaptcha.secret}")
    private String secret;

    public void validate(String response, String userIp) throws RecaptchaFailedException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("secret", secret);
        requestBody.add("response", response);
        requestBody.add("remoteip", userIp);

        HttpEntity<Map> entity   = new HttpEntity<>(requestBody, headers);
        RestTemplate    template = new RestTemplate();

        logger.info("reCAPTCHA request - response: '{}' - remoteip: '{}'", response, userIp);
        ResponseEntity<RecaptchaResponse> recaptchaResponseResponseEntity = template.postForEntity(VALIDATE_URL, entity, RecaptchaResponse.class);
        if (!recaptchaResponseResponseEntity.getStatusCode().is2xxSuccessful()) {
            String message = "reCAPTCHA request failed - HTTP Status %d".formatted(recaptchaResponseResponseEntity.getStatusCode().value());
            logger.warn(message);

            throw new RecaptchaFailedException(message);
        }

        RecaptchaResponse body = recaptchaResponseResponseEntity.getBody();
        if (!body.isSuccess()) {
            String errorCodes = Arrays.toString(body.getErrorCodes());
            String message    = "Failed to validate reCAPTCHA user response - Error codes: %s ".formatted(errorCodes);
            logger.warn(message);

            throw new RecaptchaFailedException(message, body);
        }
    }
}
