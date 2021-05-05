package de.evoila.cf.notification.repository;

import de.evoila.cf.notification.model.SMTPConfig;

import java.util.Map;

/**
 * A repository to store settings for SMTP servers.
 */
public interface SMTPConfigRepository {

    void save(SMTPConfig smtpConfig);
    Map<String, SMTPConfig> findAll();
    SMTPConfig findById(String id);
    void update(SMTPConfig smtpConfig);
    void delete(String id);
}
