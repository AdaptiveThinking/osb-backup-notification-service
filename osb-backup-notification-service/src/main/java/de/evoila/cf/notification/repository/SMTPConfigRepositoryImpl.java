package de.evoila.cf.notification.repository;

import de.evoila.cf.notification.model.SMTPConfig;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SMTPConfigRepositoryImpl implements SMTPConfigRepository {

    private final String CONFIGURATION_SMTP_HASH = "configuration.smtp";
    private final String CONFIGURATION_SMTP_ID_HASH = "configuration.smtp.id";

    private final HashOperations<String, String, SMTPConfig> hashOperations;
    private final ValueOperations<String, String> valueOperations;

    public SMTPConfigRepositoryImpl(RedisTemplate<String, SMTPConfig> smtpConfigRedisTemplate,
                                    RedisTemplate<String, String> stringRedisTemplate) {
        hashOperations = smtpConfigRedisTemplate.opsForHash();
        valueOperations = stringRedisTemplate.opsForValue();
    }

    @Override
    public void save(SMTPConfig smtpConfig) {
        Long id = valueOperations.increment(CONFIGURATION_SMTP_ID_HASH);
        smtpConfig.setId(id.toString());
        hashOperations.put(CONFIGURATION_SMTP_HASH, id.toString(), smtpConfig);
    }

    @Override
    public List<SMTPConfig> findAll() {
        Map<String, SMTPConfig> map = hashOperations.entries(CONFIGURATION_SMTP_HASH);
        return new ArrayList<>(map.values());
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
