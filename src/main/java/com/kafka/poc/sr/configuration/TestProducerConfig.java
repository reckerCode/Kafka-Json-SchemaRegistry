package com.kafka.poc.sr.configuration;

import com.kafka.poc.sr.domain.Employee;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializerConfig;
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
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSchemaSerializer.class.getName());
        configProps.put(KafkaJsonSchemaSerializerConfig.AUTO_REGISTER_SCHEMAS, false);
//        configProps.put(KafkaJsonSchemaSerializerConfig.USE_SCHEMA_ID, 3);
        configProps.put(KafkaJsonSchemaSerializerConfig.USE_LATEST_VERSION, true);
        configProps.put(KafkaJsonSchemaSerializerConfig.LATEST_COMPATIBILITY_STRICT, false);
        configProps.put(KafkaJsonSchemaSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        configProps.put(KafkaJsonSchemaSerializerConfig.ONEOF_FOR_NULLABLES, false);
        return configProps;
    }


    public ProducerFactory<String, Employee> getProducerFactory() {
        Map<String, Object> configProperties = producerFactory();
        return new DefaultKafkaProducerFactory<>(configProperties);
    }

    @Bean(value = "getEmployeeKafkaTemplate")
    public KafkaTemplate<String, Employee> getEmployeeKafkaTemplate() {
        return new KafkaTemplate<>(getProducerFactory());
    }

}
