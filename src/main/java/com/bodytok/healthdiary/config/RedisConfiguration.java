package com.bodytok.healthdiary.config;

import com.bodytok.healthdiary.domain.JwtToken;
import com.bodytok.healthdiary.repository.jwt.JwtTokenRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
@EnableRedisRepositories(
        basePackageClasses = {JwtTokenRepository.class},
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP
)
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(host, port);
        return new LettuceConnectionFactory(standaloneConfiguration);
    }

    //RefreshToken 저장을 위한 template
    @Bean
    public RedisTemplate<String, JwtToken> getRedisTemplate() {
        RedisTemplate<String, JwtToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }


    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){

        // 캐시의 기본 설정
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()  // null 값은 캐싱X
                .entryTtl(Duration.ofMinutes(10L))  // 캐시 유효 시간 설정
                .computePrefixWith(CacheKeyPrefix.simple())  // 캐시 키의 접두어를 간단하게 계산 EX) UserCacheStore::Key 의 형태로 저장
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))  // 키의 직렬화 방식 설정
                // CustomUserDetails 의 부가적인 필드들의 오류를 ignore
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer().configure(
                                objectMapper -> objectMapper.configure(
                                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false
                                )
                        )
                ));  // 값의 직렬화 방식 설정

        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        redisCacheConfigurationMap.put("AuthCacheStore", redisCacheConfiguration);
        // RedisCacheManager 리턴
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)  // Redis 연결 설정
                .cacheDefaults(redisCacheConfiguration)  // 기본 캐시 설정
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();  // RedisCacheManager 생성


    }
}