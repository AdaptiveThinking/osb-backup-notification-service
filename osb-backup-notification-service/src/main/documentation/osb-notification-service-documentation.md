# Table of Contents

- [Table of Contents](#table-of-contents)
- [Open Service Broker Backup Notification Service](#open-service-broker-backup-notification-service)
  - [Setup (local)](#setup-local)
  - [Test data](#test-data)

# Open Service Broker Backup Notification Service

## Setup (local)

The Notification Service needs a running Kafka and Redis to connect to. Environment variables need to be set, so that configurations can be downloaded from a cloud config server.

The configuration files (evoila specific) can be found in the bitbucket repository: https://bitbucket.org/evoila-group/hob-cf-service-broker-configuration/src/master/

```
SPRING_APPLICATION_NAME=FOLDER_NAME;
SPRING_CONFIG_IMPORT=optional:configserver:https://config-server-test.system.cf.hob.local;
SPRING_CLOUD_CONFIG_URI=https://config-server-test.system.cf.hob.local;
SPRING_PROFILES_ACTIVE=FILE_NAMES;
SPRING_CLOUD_CONFIG_PASSWORD=PASSWORD;
SPRING_CLOUD_CONFIG_USERNAME=USERNAME;
```

FOLDER_NAME: e.g. "local", "osb-evoila-test", etc.

FILE_NAMES: can be multiple, like "rabbitmq,mongodb,example". The prefix "application-" in filenames can be ignored. When a file is called "application-kafka.yml", then only "kafka" needs to be specified.


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
    "sendFromEmail":"EMAIL_ADDRESS",
    "sendToEmail":"EMAIL_ADDRESS",
    "template":"normal",
    "smtpConfigId":"01"
}
```

4. Create a json object with a kafka producer.

```json
{"serviceInstanceId":"0101","jobStatus":"FAILED","message":"The backup failed to execute","errorMessage":"203","startTime":"2021-02-03T10:08:02","endTime":"2021-02-03T10:09:30","executionTime":"9300"}
```


