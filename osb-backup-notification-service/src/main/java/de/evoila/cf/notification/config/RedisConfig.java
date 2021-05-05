package de.evoila.cf.notification.config;

import de.evoila.cf.notification.model.EmailNotificationConfig;
import de.evoila.cf.notification.model.SMTPConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    RedisTemplate<String, SMTPConfig> smtpConfigRedisTemplate() {
        RedisTemplate<String, SMTPConfig> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    RedisTemplate<String, EmailNotificationConfig> emailNotificationConfigRedisTemplate() {
        RedisTemplate<String, EmailNotificationConfig> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

}
