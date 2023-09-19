package com.softawii.gateway.config;

import com.softawii.gateway.filter.RecaptchaFilter;
import com.softawii.gateway.resolvers.ProxiedClientAddressResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GatewayConfig {

    private final Logger logger = LoggerFactory.getLogger(GatewayConfig.class);
    private final RedisRateLimiter defaultRateLimiter;
    private final RedisRateLimiter importantRateLimiter;
    private final RedisRateLimiter criticalRateLimiter;
    private final RecaptchaFilter  recaptchaFilter;
    @Value("${softawii.core-api.url}")
    private String coreApiUrl;

    public GatewayConfig(
            @Qualifier("default") RedisRateLimiter defaultRateLimiter,
            @Qualifier("important") RedisRateLimiter importantRateLimiter,
            @Qualifier("critical") RedisRateLimiter criticalRateLimiter,
            RecaptchaFilter recaptchaFilter
    ) {
        this.defaultRateLimiter = defaultRateLimiter;
        this.importantRateLimiter = importantRateLimiter;
        this.criticalRateLimiter = criticalRateLimiter;
        this.recaptchaFilter = recaptchaFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();

        setupCriticalRoutes(routes);
        setupImportantRoutes(routes);
        setupDefaultRoutes(routes);

        return routes.build();
    }

    private void setupDefaultRoutes(RouteLocatorBuilder.Builder builder) {
        String routeId       = "DEFAULT";
        String routeTypeName = RouteType.DEFAULT.name();
        String routes        = "/**";
        logger.info("Route ID {} - Order {} - Type {} - Method {} - Routes {}", routeId, Ordered.LOWEST_PRECEDENCE, routeTypeName, HttpMethod.values(), routes);

        builder
                .route(routeId, route -> route
                        .order(Ordered.LOWEST_PRECEDENCE)
                        .path(routes)
                        .filters(filter -> filter
                                .requestRateLimiter(rate -> rate
                                        .setRateLimiter(defaultRateLimiter)
                                        .setDenyEmptyKey(true)
                                        .setKeyResolver(new ProxiedClientAddressResolver(routeTypeName))
                                )
                                .preserveHostHeader()
                        )
                        .uri(coreApiUrl)
                );
    }

    private void setupImportantRoutes(RouteLocatorBuilder.Builder builder) {
        setupRoute(builder, RouteType.IMPORTANT, 1, Ordered.HIGHEST_PRECEDENCE, false, importantRateLimiter, HttpMethod.POST, "/post/{postId:[0-9]+}/comment", "/post/{postId:[0-9]+}/vote/{vote:0|-1|1}");
        setupRoute(builder, RouteType.IMPORTANT, 2, Ordered.HIGHEST_PRECEDENCE, false, importantRateLimiter, HttpMethod.PUT, "/post/{postId:[0-9]+}");
        setupRoute(builder, RouteType.IMPORTANT, 3, Ordered.HIGHEST_PRECEDENCE, false, importantRateLimiter, HttpMethod.DELETE, "/post/{postId:[0-9]+}", "/comment/{commentId:[0-9]+}");
    }

    private void setupCriticalRoutes(RouteLocatorBuilder.Builder builder) {
        setupRoute(builder, RouteType.CRITICAL, 1, Ordered.HIGHEST_PRECEDENCE, true, criticalRateLimiter, HttpMethod.POST, "/post");
    }

    private void setupRoute(
            RouteLocatorBuilder.Builder builder,
            RouteType routeType,
            int id,
            int order,
            boolean recaptcha,
            RedisRateLimiter redisRateLimiter,
            HttpMethod method,
            String... routes
    ) {
        String routeId = String.format("%s-%s", routeType.name(), id);
        logger.info("Route ID {} - Order {} - Type {} - Method {} - Routes {}", routeId, order, routeType.name(), method.name(), Arrays.toString(routes));
        List<GatewayFilter> filters = recaptcha ? List.of(recaptchaFilter) : List.of();

        builder
                .route(routeId, route -> route
                        .order(order)
                        .method(method).and().path(routes)
                        .filters(filter -> filter
                                .filters(filters)
                                .requestRateLimiter(rate -> rate
                                        .setRateLimiter(redisRateLimiter)
                                        .setDenyEmptyKey(true)
                                        .setKeyResolver(new ProxiedClientAddressResolver(routeType.name()))
                                )
                                .preserveHostHeader()
                        )
                        .uri(coreApiUrl)
                );
    }

}
