package de.evoila.cf.notification.model;

import java.io.Serializable;

/**
 * POJO to use when Kafka encounters deserialization errors.
 */
public class DefaultKafkaError implements Serializable {
    public String error = "A serialization error occured";
}
