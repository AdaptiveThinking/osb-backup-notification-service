package de.evoila.cf.notification.repository;

import de.evoila.cf.notification.model.SMTPConfig;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class SMTPConfigRepositoryImpl implements SMTPConfigRepository {

    private RedisTemplate<String, SMTPConfig> redisTemplate;
    private HashOperations hashOperations;
    private String CONFIGURATION_SMTP_HASH = "configuration.smtp";

    public SMTPConfigRepositoryImpl(RedisTemplate<String, SMTPConfig> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(SMTPConfig smtpConfig) {
        hashOperations.put(CONFIGURATION_SMTP_HASH, smtpConfig.getId(), smtpConfig);
    }

    @Override
    public Map<String, SMTPConfig> findAll() {
        return hashOperations.entries(CONFIGURATION_SMTP_HASH);
    }

    @Override
    public SMTPConfig findById(String id) {
        return (SMTPConfig) hashOperations.get(CONFIGURATION_SMTP_HASH, id);
    }

    @Override
    public void update(SMTPConfig smtpConfig) {
        save(smtpConfig);
    }

    @Override
    public void delete(String id) {
        hashOperations.delete(CONFIGURATION_SMTP_HASH, id);
    }
}
