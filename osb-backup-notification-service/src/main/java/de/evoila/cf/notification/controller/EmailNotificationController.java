package de.evoila.cf.notification.controller;

import de.evoila.cf.notification.model.EmailNotificationConfig;
import de.evoila.cf.notification.model.SMTPConfig;
import de.evoila.cf.notification.repository.EmailNotificationConfigRepositoryImpl;
import de.evoila.cf.notification.repository.SMTPConfigRepositoryImpl;
import de.evoila.cf.notification.service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Api(value = "/emailNotification", description = "Manage how and where e-mail notifications will be sent.")
@Controller
public class EmailNotificationController {

    @Autowired
    private SMTPConfigRepositoryImpl smtpConfigRepository;

    @Autowired
    private EmailNotificationConfigRepositoryImpl emailNotificationConfigRepository;

    @Autowired
    private MailService mailService;

    @ApiOperation(value = "Add a listener. Notifications will be sent to the specified e-mail when the status appears.")
    @RequestMapping(value = "/emailNotification", method = RequestMethod.GET)
    public ResponseEntity<List<EmailNotificationConfig>> getAllEmailNotificationConfig() {
        List<EmailNotificationConfig> allConfigs = emailNotificationConfigRepository.findAll();
        if (allConfigs != null)
            return new ResponseEntity<>(allConfigs, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Add a listener. Notifications will be sent to the specified e-mail when the status appears.")
    @RequestMapping(value = "/emailNotification", method = RequestMethod.POST)
    public ResponseEntity<EmailNotificationConfig> createListener(@RequestBody EmailNotificationConfig emailNotificationConfig) {
        emailNotificationConfigRepository.save(emailNotificationConfig);
        return new ResponseEntity<>(emailNotificationConfig, HttpStatus.OK);
    }

    @ApiOperation(value = "Get a configured notification setting by the specified ID.")
    @RequestMapping(value = "/emailNotification/{configID}", method = RequestMethod.GET)
    public ResponseEntity<EmailNotificationConfig> getConfigurationByID(@PathVariable String configID) {
        EmailNotificationConfig emailNotificationConfig = emailNotificationConfigRepository.findById(configID);
        if (emailNotificationConfig != null)
            return new ResponseEntity<>(emailNotificationConfig, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Delete a configured notification setting by the specified ID.")
    @RequestMapping(value = "/emailNotification/{configID}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteConfigurationByID(@PathVariable String configID) {
        emailNotificationConfigRepository.deleteEmailNotificationConfig(configID);
        return new ResponseEntity<>("Config deleted.", HttpStatus.OK);
    }

    @ApiOperation(value = "Get all configured notification settings from the specified instance.")
    @RequestMapping(value = "/emailNotification/byInstance/{instanceID}", method = RequestMethod.GET)
    public ResponseEntity<List<EmailNotificationConfig>> getConfigurationsByServiceInstance(@PathVariable String instanceID) {
        List<EmailNotificationConfig> allConfigsByInstance = emailNotificationConfigRepository.findAllByInstance(instanceID);
        if (allConfigsByInstance.size() != 0)
            return new ResponseEntity<>(allConfigsByInstance, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Delete all configured notification settings from the specified instance.")
    @RequestMapping(value = "/emailNotification/byInstance/{instanceID}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteConfigurationsByServiceInstance(@PathVariable String instanceID) {
        emailNotificationConfigRepository.deleteByInstance(instanceID);
        return new ResponseEntity("All config deleted.", HttpStatus.OK);
    }

    @ApiOperation(value = "Get all e-mail configurations with the specified e-mail address.")
    @RequestMapping(value = "/emailNotification/byEmail/{emailAddress}", method = RequestMethod.GET)
    public ResponseEntity<String> getConfigurationsByEmailAddress() {
        //TODO
        return new ResponseEntity("TODO", HttpStatus.OK);
    }

    @ApiOperation(value = "Delete all e-mail configurations with the specified e-mail address.")
    @RequestMapping(value = "/emailNotification/byEmail/{emailAddress}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteConfigurationsByEmailAddress() {
        //TODO
        return new ResponseEntity("TODO", HttpStatus.OK);
    }

    @ApiOperation(value = "Send a test e-mail.")
    @RequestMapping(value = "/emailNotification/testEmail", method = RequestMethod.POST)
    public ResponseEntity<String> sendTestEmail() {
        SMTPConfig smtpConfig = smtpConfigRepository.findById("01");

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("successdataproject@gmail.com");

        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");

        mailService.sendEMail(smtpConfig, msg);

        return new ResponseEntity("Created", HttpStatus.OK);
    }
}
