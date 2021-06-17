package de.evoila.cf.notification.service;

import de.evoila.cf.notification.model.EmailNotificationConfig;
import de.evoila.cf.notification.model.SMTPConfig;
import de.evoila.cf.notification.model.JobMessage;
import de.evoila.cf.notification.model.Mail;
import de.evoila.cf.notification.repository.SMTPConfigRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class MailService {

    @Autowired
    private SMTPConfigRepositoryImpl smtpConfigRepository;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    public boolean sendEMail(SMTPConfig smtpConfig, SimpleMailMessage msg) {
        getJavaMailSender(smtpConfig).send(msg);
        return true; // TODO test if successful
    }

    /**
     * Send an email with information detailing the status of a job.
     *
     * @param emailNotificationConfig containing information where to send the email to.
     * @param jobMessage status of the job with information
     * @return if the email has been successfully sent
     */
    public boolean sendEmail(EmailNotificationConfig emailNotificationConfig, JobMessage jobMessage) {

        SMTPConfig smtpConfig = smtpConfigRepository.findById(emailNotificationConfig.getSmtpConfigId());
        if(smtpConfig != null){
            Mail mail = createMail(emailNotificationConfig,jobMessage);
            String htmlContent = getHtmlContent(mail);
            JavaMailSender javaMailSender = getJavaMailSender(smtpConfig);
            MimeMessage message = javaMailSender.createMimeMessage();

            try {
                MimeMessageHelper helper = new MimeMessageHelper(message,
                        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                        StandardCharsets.UTF_8.name());

                helper.setFrom(mail.getFrom());
                helper.setTo(mail.getTo());
                helper.setSubject(mail.getSubject());
                helper.setText(htmlContent, true);

            } catch (MessagingException ex) {
                ex.printStackTrace();
                return false;
            }

            javaMailSender.send(message);
            return true;
        }
        return false;
    }

    /**
     * Set up a JavaMailSender with the given SMTPConfig.
     *
     * @param smtpConfig the configurations for the JavaMailSender
     * @return the configured JavaMailSender
     */
    private JavaMailSender getJavaMailSender(SMTPConfig smtpConfig) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpConfig.getHost());
        mailSender.setPort(smtpConfig.getPort());
        mailSender.setUsername(smtpConfig.getUsername());
        mailSender.setPassword(smtpConfig.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.connectiontimeout", 5000);
        props.put("mail.smtp.timeout", 5000);
        props.put("mail.smtp.writetimeout", 5000);
        //props.put("mail.debug", "true");

        return mailSender;
    }

    /**
     * Create a Mail object containing variables and other configuration on how to fill the content of an email.
     *
     * @param emailNotificationConfig
     * @param jobMessage
     * @return
     */
    private Mail createMail(EmailNotificationConfig emailNotificationConfig, JobMessage jobMessage){
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("serviceInstanceId", jobMessage.getServiceInstanceId());
        properties.put("jobStatus", jobMessage.getJobStatus());
        properties.put("jobType", "TODO jobType"); //todo
        properties.put("cause", jobMessage.getMessage());
        String subject = "Service Instance " + jobMessage.getServiceInstanceId() + " finished job with status: " + jobMessage.getJobStatus();

        return new Mail(emailNotificationConfig.getSendFromEmail(),
                emailNotificationConfig.getSendToEmail(),
                subject,
                new Mail.HtmlTemplate(emailNotificationConfig.getTemplate(), properties));
    }

    private String getHtmlContent(Mail mail){
        Context context = new Context();
        context.setVariables(mail.getHtmlTemplate().getProps());
        return springTemplateEngine.process(mail.getHtmlTemplate().getTemplate(), context);
    }

}
