package com.spring.course.management.system.config;


import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.Bucket4jLettuce;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory) {

        RedisCacheConfiguration configuration =
                RedisCacheConfiguration
                        .defaultCacheConfig()
                        .entryTtl(
                                Duration.ofMinutes(10)
                        )
                        .serializeValuesWith(
                                RedisSerializationContext
                                        .SerializationPair
                                        .fromSerializer(
                                                new GenericJackson2JsonRedisSerializer()
                                        )
                        );

        return RedisCacheManager.builder(
                        redisConnectionFactory
                )
                .cacheDefaults(configuration)
                .build();
    }

    @Bean
    public ProxyManager<String> bucket4jProxyManager() {

        RedisURI redisURI =
                RedisURI.builder()
                        .withHost("localhost")
                        .withPort(6379)
                        .build();

        RedisClient redisClient =
                RedisClient.create(redisURI);

        StatefulRedisConnection<String, byte[]> connection =
                redisClient.connect(
                        io.lettuce.core.codec.RedisCodec.of(
                                io.lettuce.core.codec.StringCodec.UTF8,
                                io.lettuce.core.codec.ByteArrayCodec.INSTANCE
                        )
                );

        return Bucket4jLettuce
                .casBasedBuilder(connection)
                .expirationAfterWrite(
                        ExpirationAfterWriteStrategy
                                .basedOnTimeForRefillingBucketUpToMax(
                                        Duration.ofMinutes(1)
                                )
                )
                .build();
    }
}