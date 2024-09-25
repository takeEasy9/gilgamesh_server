package com.gilgamesh.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description redis configuration
 * @createDate 2024/5/2 1:03
 * @since 1.0.0
 */
@Configuration
public class RedisCacheConfiguration {
    private final Logger logger = LoggerFactory.getLogger(RedisCacheConfiguration.class);


    /**
     * 配置 redisTemplate 代替默认配置
     *
     * @param redisConnectionFactory RedisConnectionFactory
     * @return RedisTemplate<Object, Object>
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        logger.info("初始化 redis 配置信息");
        RedisSerializer<?> keyRedisSerializer = new StringRedisSerializer();
        RedisSerializer<Object> valueRedisSerializer = valueRedisSerializer();

        // 配置 redisTemplate
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置 key 的序列化方式
        redisTemplate.setKeySerializer(keyRedisSerializer);
        // 设置 value 的序列化方式
        redisTemplate.setValueSerializer(valueRedisSerializer);
        // 设置 hash key 的序列化方式
        redisTemplate.setHashKeySerializer(keyRedisSerializer);
        // 设置 hash value 的序列化方式
        redisTemplate.setHashValueSerializer(valueRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 使用 jackson 序列化器
     *
     * @return Jackson2JsonRedisSerializer<Object>
     */
    private RedisSerializer<Object> valueRedisSerializer() {
        // 解决查询缓存转换异常的问题
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        // 使用 Jackson2JsonRedisSerialize 替换默认JDK序列化
        return new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
    }
}
