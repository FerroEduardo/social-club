package com.softawii.gateway.resolvers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

public class ProxiedClientAddressResolver implements KeyResolver {

    private final Logger logger = LoggerFactory.getLogger(ProxiedClientAddressResolver.class);

    private static final int    MAX_TRUSTED_INDEX = 1;
    private final        String id;

    public ProxiedClientAddressResolver(String id) {
        this.id = id;
    }

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        XForwardedRemoteAddressResolver resolver          = XForwardedRemoteAddressResolver.maxTrustedIndex(MAX_TRUSTED_INDEX);
        InetSocketAddress               inetSocketAddress = resolver.resolve(exchange);
        String                          hostAddress       = inetSocketAddress.getAddress().getHostAddress();

        return Mono.just(String.format("%s-%s", hostAddress, id));
    }
}