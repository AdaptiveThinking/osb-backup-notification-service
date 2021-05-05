package de.evoila.cf.notification.model;

import de.evoila.cf.notification.model.enums.JobStatus;

import java.io.Serializable;

/**
 * Configuration on where, how and when to send an e-mail.
 */
public class EmailNotificationConfig implements Serializable {

    public String id;
    public JobStatus triggerOn; // send email when this jobstatus is reached
    public String serviceInstanceID; // the service instance doing backup related jobs
    public String sendFromEmail; // where to send the email
    public String sendToEmail; // where to send the email
    public String template; // email template
    public String smtpConfigId; // which smtp should be used to send the email

    public EmailNotificationConfig() {
    }

    public EmailNotificationConfig(String id, JobStatus triggerOn, String serviceInstanceID, String sendFromEmail, String sendToEmail, String template, String smtpConfigId) {
        this.id = id;
        this.triggerOn = triggerOn;
        this.serviceInstanceID = serviceInstanceID;
        this.sendFromEmail = sendFromEmail;
        this.sendToEmail = sendToEmail;
        this.template = template;
        this.smtpConfigId = smtpConfigId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JobStatus getTriggerOn() {
        return triggerOn;
    }

    public void setTriggerOn(JobStatus triggerOn) {
        this.triggerOn = triggerOn;
    }

    public String getServiceInstanceID() {
        return serviceInstanceID;
    }

    public void setServiceInstanceID(String serviceInstanceID) {
        this.serviceInstanceID = serviceInstanceID;
    }

    public String getSendFromEmail() {
        return sendFromEmail;
    }

    public void setSendFromEmail(String sendFromEmail) {
        this.sendFromEmail = sendFromEmail;
    }

    public String getSendToEmail() {
        return sendToEmail;
    }

    public void setSendToEmail(String sendToEmail) {
        this.sendToEmail = sendToEmail;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSmtpConfigId() {
        return smtpConfigId;
    }

    public void setSmtpConfigId(String smtpConfigId) {
        this.smtpConfigId = smtpConfigId;
    }
}
