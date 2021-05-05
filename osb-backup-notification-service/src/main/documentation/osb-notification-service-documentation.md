# Table of Contents

- [Table of Contents](#table-of-contents)
- [Open Service Broker Notification Service](#open-service-broker-notification-service)
  - [Test data](#test-data)

# Open Service Broker Notification Service

## Test data

The email service can be tested with:

1. Configure a gmail SMTP server.

POST /SMTPConfiguration
```json
{
    "id":"01",
    "host":"smtp.gmail.com",
    "port":587,
    "username":"EMAIL_USERNAME",
    "password":"EMAIL_PASSWORD",
    "from":null
}
```

2. Test if configuration has been created.

GET /SMTPConfiguration

3. Configure a notification listener.

POST /emailNotification
```json
{
    "id":"0",
    "triggerOn":"FAILED",
    "serviceInstanceID":"0101",
    "sendToEmail":"EMAIL_ADDRESS",
    "template":"normal",
    "smtpConfigId":"01"
}
```

4. Create a json object with a kafka producer.

```json
{"serviceInstanceId":"0101","jobStatus":"FAILED","message":"The backup failed to execute","errorMessage":"203","startTime":"2021-02-03 10:08:02","endTime":"2021-02-03 10:09:30","executionTime":"9300"}
```

