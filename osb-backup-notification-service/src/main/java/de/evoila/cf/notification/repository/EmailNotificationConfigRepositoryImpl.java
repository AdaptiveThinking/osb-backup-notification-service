package de.evoila.cf.notification.repository;

import de.evoila.cf.notification.model.EmailNotificationConfig;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Repository for storing Notification settings in a Redis database.
 * <p>
 * TODO return boolean, exception or something else when operations fail
 */
@Repository
public class EmailNotificationConfigRepositoryImpl implements EmailNotificationConfigRepository {

    private final String SERVICE_INSTANCE_HASH = "serviceinstance";
    private final String SERVICE_INSTANCE_ID_HASH = "serviceinstance.id";

    private final RedisTemplate<String, EmailNotificationConfig> emailNotificationConfigRedisTemplate;

    private final HashOperations<String, String, EmailNotificationConfig> hashOperations;
    private final SetOperations<String, String> setOperations;
    private final ValueOperations<String, String> valueOperations;

    public EmailNotificationConfigRepositoryImpl(
            RedisTemplate<String, EmailNotificationConfig> emailNotificationConfigRedisTemplate,
            RedisTemplate<String, String> stringRedisTemplate) {
        this.hashOperations = emailNotificationConfigRedisTemplate.opsForHash();
        this.setOperations = stringRedisTemplate.opsForSet();
        this.valueOperations = stringRedisTemplate.opsForValue();

        this.emailNotificationConfigRedisTemplate = emailNotificationConfigRedisTemplate;
    }

    /**
     * Store the config inside the hash and add the service instance id to a set. The set is for indexing all service
     * instances with configured notifications, which is required when fetching all configured service instances.
     *
     * @param emailNotificationConfig the config to be saved
     */
    @Override
    public void save(EmailNotificationConfig emailNotificationConfig) {
        Long id = valueOperations.increment(SERVICE_INSTANCE_ID_HASH);
        String hashKey = SERVICE_INSTANCE_HASH + ":" + emailNotificationConfig.getServiceInstanceID();
        emailNotificationConfig.setId(id.toString());
        hashOperations.put(hashKey, id.toString(), emailNotificationConfig);
        setOperations.add(SERVICE_INSTANCE_HASH, hashKey);
    }

    @Override
    public EmailNotificationConfig findById(String id) {
        for (EmailNotificationConfig config : findAll()) {
            if (config.getId().equals(id)) {
                return config;
            }
        }
        return null;
    }

    @Override
    public List<EmailNotificationConfig> findAll() {
        Set<String> allServiceInstances = setOperations.members(SERVICE_INSTANCE_HASH);
        Map<String, EmailNotificationConfig> allEmailNotifications = new HashMap<>();

        if (allServiceInstances != null) {
            for (String hash : allServiceInstances) {
                allEmailNotifications.putAll(hashOperations.entries(hash));
            }
        }

        return new ArrayList<EmailNotificationConfig>(allEmailNotifications.values());
    }

    @Override
    public List<EmailNotificationConfig> findAllByInstance(String instanceId) {
        Map<String, EmailNotificationConfig> map = hashOperations.entries(SERVICE_INSTANCE_HASH + ":" + instanceId);
        return new ArrayList<EmailNotificationConfig>(map.values());
    }

    @Override
    public void update(EmailNotificationConfig emailNotificationConfig) {
        save(emailNotificationConfig);
    }

    /**
     * Search for the EmailNotificationConfig and remove them from the service instance and index.
     *
     * @param id the id of the EmailNotificationConfig
     */
    @Override
    public void deleteEmailNotificationConfig(String id) {
        EmailNotificationConfig emailNotificationConfig = findById(id);
        String hashKey = SERVICE_INSTANCE_HASH + ":" + emailNotificationConfig.getServiceInstanceID();
        hashOperations.delete(hashKey, emailNotificationConfig.getId());
        if (hashOperations.size(hashKey) == 0) {
            setOperations.remove(SERVICE_INSTANCE_HASH, hashKey);
        }
    }

    /**
     * Remove all EmailNotificationConfig from the specified service instance.
     *
     * @param instanceId the id of the service instance
     */
    @Override
    public void deleteByInstance(String instanceId) {
        String hashKey = SERVICE_INSTANCE_HASH + ":" + instanceId;

        Set<String> allConfigIds = hashOperations.keys(hashKey);
        setOperations.remove(SERVICE_INSTANCE_HASH, allConfigIds.toArray());
        emailNotificationConfigRedisTemplate.delete(hashKey);
    }
}
