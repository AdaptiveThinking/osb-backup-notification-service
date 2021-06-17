package de.evoila.cf.notification.listener;

import de.evoila.cf.notification.config.KafkaConfiguration;
import de.evoila.cf.notification.model.EmailNotificationConfig;
import de.evoila.cf.notification.model.JobMessage;
import de.evoila.cf.notification.repository.EmailNotificationConfigRepositoryImpl;
import de.evoila.cf.notification.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class KafkaJobListener {

    Logger logger = LoggerFactory.getLogger(KafkaJobListener.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private EmailNotificationConfigRepositoryImpl emailNotificationConfigRepository;

    @KafkaListener(topics = KafkaConfiguration.TOPIC_NAME,
            groupId = KafkaConfiguration.CONSUMER_GROUP,
            containerFactory = "jobMessageKafkaListenerFactory")
    public void listen(JobMessage jobMessage, Acknowledgment acknowledgment) {
        List<EmailNotificationConfig> list = emailNotificationConfigRepository.findAllByInstance(jobMessage.getServiceInstanceId());

        for (EmailNotificationConfig emailNotificationConfig : list) {
            if (emailNotificationConfig.getTriggerOn() == jobMessage.getJobStatus()) {
                logger.debug("EmailNotificationConfig "
                        + emailNotificationConfig.getId()
                        + " sending Email with SMTPConfig "
                        + emailNotificationConfig.getSmtpConfigId());
                mailService.sendEmail(emailNotificationConfig, jobMessage);
            }
        }
        acknowledgment.acknowledge();
    }
}
