package de.evoila.cf.notification.model;

import de.evoila.cf.notification.model.enums.JobStatus;

import java.util.Date;

/**
 * Class mapping to kafka json messages.
 */
public class JobMessage {
    public String serviceInstanceId;
    public JobStatus jobStatus;
    public String message;
    public String errorMessage;
    public Date startTime;
    public Date endTime;
    public long executionTime;

    public JobMessage() {
    }

    public JobMessage(String serviceInstanceId, JobStatus jobStatus, String message, String errorMessage, Date startTime, Date endTime, long executionTime) {
        this.serviceInstanceId = serviceInstanceId;
        this.jobStatus = jobStatus;
        this.message = message;
        this.errorMessage = errorMessage;
        this.startTime = startTime;
        this.endTime = endTime;
        this.executionTime = executionTime;
    }

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
}
