package com.softawii.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RedisRateLimiterConfig {

    @Value("${softawii.production}")
    private boolean isProduction;

    @Bean("default")
    @Primary
    public RedisRateLimiter defaultRateLimiter() {
        RedisRateLimiter redisRateLimiter = new RedisRateLimiter(10, 10, 1);
        redisRateLimiter.setIncludeHeaders(!isProduction);

        return redisRateLimiter;
    }

    @Bean("critical")
    public RedisRateLimiter criticalRateLimiter() {
        /*
        Rate limits bellow 1 request/s are accomplished by setting replenishRate to the wanted number of requests,
        requestedTokens to the timespan in seconds and burstCapacity to the product of replenishRate and requestedTokens,
        e.g. setting replenishRate=1, requestedTokens=60 and burstCapacity=60 will result in a limit of 1 request/min.
        https://stackoverflow.com/questions/67519440/spring-cloud-redis-rate-limiting-not-working-for-request-per-minute
        */
        RedisRateLimiter redisRateLimiter = new RedisRateLimiter(1, 60, 60);
        redisRateLimiter.setIncludeHeaders(!isProduction);

        return redisRateLimiter;
    }

    @Bean("important")
    public RedisRateLimiter importantRateLimiter() {
        RedisRateLimiter redisRateLimiter = new RedisRateLimiter(5, 25, 5);
        redisRateLimiter.setIncludeHeaders(!isProduction);

        return redisRateLimiter;
    }
}
