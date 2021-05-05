package de.evoila.cf.notification.controller;

import de.evoila.cf.notification.model.SMTPConfig;
import de.evoila.cf.notification.repository.SMTPConfigRepositoryImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Api(value = "/SMTPConfiguration", description = "Configure Email SMTP server.")
@Controller
public class SMTPConfigurationController {

    SMTPConfigRepositoryImpl smtpConfigurationRepository;

    public SMTPConfigurationController(SMTPConfigRepositoryImpl smtpConfigurationRepository){
        this.smtpConfigurationRepository = smtpConfigurationRepository;
    }

    @ApiOperation(value = "Get all configured SMTP server.")
    @RequestMapping(value ="/SMTPConfiguration", method = RequestMethod.GET)
    public ResponseEntity<Map<String, SMTPConfig>> getConfigurations(){
        Map<String, SMTPConfig> configMap = smtpConfigurationRepository.findAll();
        return new ResponseEntity<Map<String, SMTPConfig>>(configMap, HttpStatus.OK);
    }

    @ApiOperation(value = "Add an SMTP server configuration.")
    @RequestMapping(value ="/SMTPConfiguration", method = RequestMethod.POST)
    public ResponseEntity<SMTPConfig> addSMTPConfiguration(@RequestBody SMTPConfig smtpConfig){
        smtpConfigurationRepository.save(smtpConfig);
        return new ResponseEntity<SMTPConfig>(smtpConfig, HttpStatus.CREATED);
    }
}
