package com.softawii.gateway.filter;

import com.softawii.gateway.exception.RecaptchaFailedException;
import com.softawii.gateway.resolvers.ProxiedClientAddressResolver;
import com.softawii.gateway.service.RecaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class RecaptchaFilter implements GatewayFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(RecaptchaFilter.class);

    private final RecaptchaService service;

    public RecaptchaFilter(RecaptchaService service) {
        this.service = service;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String      userIpAddress = ProxiedClientAddressResolver.getUserIpAddress(exchange);
        HttpHeaders headers       = exchange.getRequest().getHeaders();

        List<String> recaptchaHeaderValues = headers.getOrEmpty("g-recaptcha-response");
        if (recaptchaHeaderValues.size() != 1) {
            logger.warn("Missing 'g-recaptcha-response' header");
            throw new RecaptchaFailedException("Missing 'g-recaptcha-response' header");
        }
        String recaptchaResponse = recaptchaHeaderValues.get(0);
        service.validate(recaptchaResponse, userIpAddress);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
