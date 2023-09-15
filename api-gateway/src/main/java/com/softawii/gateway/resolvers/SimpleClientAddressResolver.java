package com.softawii.gateway.resolvers;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;

public class SimpleClientAddressResolver implements KeyResolver {
    private final String id;

    public SimpleClientAddressResolver(String id) {
        this.id = id;
    }

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                .map(InetSocketAddress::getAddress)
                .map(InetAddress::getHostAddress)
                .map(hostAddress -> Mono.just(String.format("%s-%s", hostAddress, id)))
                .orElse(Mono.empty());
    }
}