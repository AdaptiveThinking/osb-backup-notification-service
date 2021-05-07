package de.evoila.cf.notification.repository;

import de.evoila.cf.notification.model.EmailNotificationConfig;

import java.util.List;
import java.util.Map;

/**
 * A repository to store notification settings. In each config is described what and where to send a notification to,
 * and when the notification gets triggered.
 */
public interface EmailNotificationConfigRepository {

    void save(EmailNotificationConfig emailNotificationConfig);
    EmailNotificationConfig findById(String id);
    List<EmailNotificationConfig> findAll();
    List<EmailNotificationConfig> findAllByInstance(String instanceId);
    void update(EmailNotificationConfig emailNotificationConfig);
    void deleteEmailNotificationConfig(String id);
    void deleteByInstance(String instanceId);

}
