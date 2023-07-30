package com.kafka.poc.sr.configuration;

import com.kafka.poc.sr.domain.Developer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializerConfig;
import io.confluent.kafka.serializers.subject.RecordNameStrategy;
import io.confluent.kafka.serializers.subject.TopicNameStrategy;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TestProducerConfig {

    @Bean
    public Map<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomJsonSchemaSerializer.class.getName());
        configProps.put(KafkaJsonSchemaSerializerConfig.KEY_SUBJECT_NAME_STRATEGY, TopicNameStrategy.class.getName());
        configProps.put(KafkaJsonSchemaSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY_DEFAULT, TopicNameStrategy.class.getName());
        configProps.put(KafkaJsonSchemaSerializerConfig.AUTO_REGISTER_SCHEMAS, false);
        configProps.put(KafkaJsonSchemaSerializerConfig.USE_LATEST_VERSION, true);
        configProps.put(KafkaJsonSchemaSerializerConfig.LATEST_COMPATIBILITY_STRICT, true);
        configProps.put(KafkaJsonSchemaSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        configProps.put(KafkaJsonSchemaSerializerConfig.ONEOF_FOR_NULLABLES, false);
        return configProps;
    }


    public ProducerFactory<String, Developer> getProducerFactory() {
        Map<String, Object> configProperties = producerFactory();
        return new DefaultKafkaProducerFactory<>(configProperties);
    }

    @Bean(value = "getEmployeeKafkaTemplate")
    public KafkaTemplate<String, Developer> getEmployeeKafkaTemplate() {
        return new KafkaTemplate<>(getProducerFactory());
    }

}
