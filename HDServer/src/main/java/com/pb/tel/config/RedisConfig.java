package com.pb.tel.config;

import com.pb.tel.data.adapters.RedisDefaultSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.logging.Logger;

/**
 * Created by vladimir on 18.07.19.
 */

@Configuration
public class RedisConfig  {

    private static final Logger log = Logger.getLogger(RedisConfig.class.getCanonicalName());

    @Autowired
    Environment environment;

    @Bean(name="redisConnectionFactory")
    public JedisConnectionFactory redisConnectionFactory() {

        log.info("redis.max.total: "+environment.getProperty("redis.max.total"));
        log.info("redis.max.idle: "+environment.getProperty("redis.max.idle"));
        log.info("redis.min.idle: "+environment.getProperty("redis.min.idle"));
        log.info("redis.max.wait.millis: "+environment.getProperty("redis.max.wait.millis"));
        log.info("redis.test.on.borrow: "+environment.getProperty("redis.test.on.borrow"));
        log.info("redis.host: "+environment.getProperty("redis.host"));
        log.info("redis.port: "+environment.getProperty("redis.port"));
        log.info("redis.timeout: "+environment.getProperty("redis.timeout"));


        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(Integer.valueOf(environment.getProperty("redis.max.total")));
        poolConfig.setMaxIdle(Integer.valueOf(environment.getProperty("redis.max.idle")));
        poolConfig.setMinIdle(Integer.valueOf(environment.getProperty("redis.min.idle")));
        poolConfig.setMaxWaitMillis(Integer.valueOf(environment.getProperty("redis.max.wait.millis")));
        poolConfig.setTestOnBorrow(Boolean.valueOf(environment.getProperty("redis.test.on.borrow")));

        JedisConnectionFactory factory = new JedisConnectionFactory(poolConfig);
        factory.setHostName(environment.getProperty("redis.host"));
        factory.setPort(Integer.valueOf(environment.getProperty("redis.port")));
        factory.setTimeout(Integer.valueOf(environment.getProperty("redis.timeout")));
        factory.setUsePool(true);

        return factory;
    }


    @Bean(name = "redisTemplate")
    public RedisTemplate<?,?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<?,?> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setDefaultSerializer(new RedisDefaultSerializer());
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        return template;
    }
}
