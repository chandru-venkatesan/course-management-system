package com.spring.course.management.system.rate;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private final ProxyManager<String> proxyManager;

    @Value("${rate.limit.requests}")
    private long authMaxRequests;

    @Value("${rate.limit.duration-minutes}")
    private long authDurationMinutes;

    @Value("${rate.limit.api.requests}")
    private long apiMaxRequests;

    @Value("${rate.limit.api.duration-minutes}")
    private long apiDurationMinutes;

    public RateLimitService(
            ProxyManager<String> proxyManager) {

        this.proxyManager = proxyManager;
    }

    public ConsumptionProbe tryConsumeAuth(String key) {

        BucketConfiguration configuration =
                BucketConfiguration.builder()
                        .addLimit(
                                Bandwidth.classic(
                                        authMaxRequests,
                                        Refill.greedy(
                                                authMaxRequests,
                                                Duration.ofMinutes(
                                                        authDurationMinutes
                                                )
                                        )
                                )
                        )
                        .build();

        Bucket bucket =
                proxyManager.getProxy(
                        key,
                        () -> configuration
                );

        return bucket.tryConsumeAndReturnRemaining(1);
    }

    public ConsumptionProbe tryConsumeApi(String key) {

        BucketConfiguration configuration =
                BucketConfiguration.builder()
                        .addLimit(
                                Bandwidth.classic(
                                        apiMaxRequests,
                                        Refill.greedy(
                                                apiMaxRequests,
                                                Duration.ofMinutes(
                                                        apiDurationMinutes
                                                )
                                        )
                                )
                        )
                        .build();

        Bucket bucket =
                proxyManager.getProxy(
                        key,
                        () -> configuration
                );

        return bucket.tryConsumeAndReturnRemaining(1);
    }
}