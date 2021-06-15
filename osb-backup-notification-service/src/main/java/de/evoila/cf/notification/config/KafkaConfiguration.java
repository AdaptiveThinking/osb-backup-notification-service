package de.evoila.cf.notification.config;

import de.evoila.cf.notification.model.JobMessage;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfiguration {

    public static final String TOPIC_NAME = "Backup-JobEvent-TestTopic";
    public static final String CONSUMER_GROUP = "Consumer-NotificationService-JobMessage";

    @Autowired
    private KafkaProperties kafkaProperties;

    /**
     * Configure a JobMessage ConsumerFactory. JSON data from the Kafka stream will be converted into objects.
     * @return the ConsumerFactory
     */
    @Bean
    public ConsumerFactory<String, JobMessage> jobMessageConsumerFactory() {
        Map<String, Object> config = kafkaProperties.buildConsumerProperties();

        config.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        //handle deserialization errors
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, JobMessage.class);
        // only read new messages

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, JobMessage> jobMessageKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, JobMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(jobMessageConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    @Bean
    public NewTopic backupJob() {
        return TopicBuilder.name(TOPIC_NAME)
                .build();
    }
}
