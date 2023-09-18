package com.softawii.gateway.resolvers;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

public class ProxiedClientAddressResolver implements KeyResolver {

    private static final int    MAX_TRUSTED_INDEX = 1;
    private final        String id;

    public ProxiedClientAddressResolver(String id) {
        this.id = id;
    }

    public static String getUserIpAddress(ServerWebExchange exchange) {
        XForwardedRemoteAddressResolver resolver          = XForwardedRemoteAddressResolver.maxTrustedIndex(MAX_TRUSTED_INDEX);
        InetSocketAddress               inetSocketAddress = resolver.resolve(exchange);
        return inetSocketAddress.getAddress().getHostAddress();
    }

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        String hostAddress = getUserIpAddress(exchange);

        return Mono.just(String.format("%s-%s", hostAddress, id));
    }
}