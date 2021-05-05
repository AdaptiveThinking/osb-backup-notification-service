package de.evoila.cf.notification.listener;

import de.evoila.cf.notification.model.EmailNotificationConfig;
import de.evoila.cf.notification.model.JobMessage;
import de.evoila.cf.notification.repository.EmailNotificationConfigRepositoryImpl;
import de.evoila.cf.notification.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaJobListener {

    @Autowired
    private MailService mailService;

    @Autowired
    private EmailNotificationConfigRepositoryImpl emailNotificationConfigRepository;


    @KafkaListener(topics = "backup-job", groupId = "jobMessage_json", containerFactory = "jobMessageKafkaListenerFactory")
    public void listen(JobMessage jobMessage) {

        Map<String, EmailNotificationConfig> emailNotificationConfigMap = emailNotificationConfigRepository.findAllByInstance(jobMessage.getServiceInstanceId());

        for (EmailNotificationConfig emailNotificationConfig : emailNotificationConfigMap.values()) {
            if(emailNotificationConfig.getTriggerOn() == jobMessage.getJobStatus()){
                mailService.sendEmail(emailNotificationConfig, jobMessage);
            }
        }
    }
}
