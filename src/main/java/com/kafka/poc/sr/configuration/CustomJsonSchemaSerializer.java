package com.kafka.poc.sr.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.schemaregistry.json.JsonSchemaProvider;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializerConfig;
import io.confluent.kafka.serializers.subject.RecordNameStrategy;
import io.confluent.kafka.serializers.subject.TopicNameStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

@Slf4j
public class CustomJsonSchemaSerializer<T> implements Serializer<T> {

    private ObjectMapper objectMapper;

    @Autowired
    private SchemaRegistryClient schemaRegistryClient;


    public Map<String, Object> getSchemaRegistryConfig() {
        Map<String, Object> map = new HashMap();
        map.put(KafkaJsonSchemaSerializerConfig.AUTO_REGISTER_SCHEMAS, false);
        map.put(KafkaJsonSchemaSerializerConfig.USE_LATEST_VERSION, true);
        map.put(KafkaJsonSchemaSerializerConfig.USE_SCHEMA_ID, 13);
        map.put(KafkaJsonSchemaSerializerConfig.LATEST_COMPATIBILITY_STRICT, true);
        map.put(KafkaJsonSchemaSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        map.put(KafkaJsonSchemaSerializerConfig.ONEOF_FOR_NULLABLES, false);
        return map;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Initialize ObjectMapper
        objectMapper = new ObjectMapper();

        // Initialize Schema Registry Client
        String schemaRegistryUrl = (String) configs.get("schema.registry.url");
        schemaRegistryClient = new CachedSchemaRegistryClient(schemaRegistryUrl, 100,
                new ArrayList<>(Arrays.asList(new JsonSchemaProvider())), getSchemaRegistryConfig());
    }

    @Override
    public byte[] serialize(String topic, T data) {
        return serialize(topic, null, data);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, T data) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Fetch the latest version of the schema from the Schema Registry
            String schema = schemaRegistryClient.getLatestSchemaMetadata(topic + "-value").getSchema();
            log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema));
            // Validate object against schema
            com.networknt.schema.JsonSchema validatorSchema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schema);
            JsonNode objectJsonNode = objectMapper.valueToTree(data);
            Set<ValidationMessage> validationResult = validatorSchema.validate(objectJsonNode);
            log.info("validationResult: {}", validationResult);
            // Throw an exception if the object is not valid
            if (!validationResult.isEmpty()) {
                throw new RuntimeException("Object failed JSON schema validation: " + validationResult.toString());
            }
            // Serialize the object into a JSON byte array
            log.info("Schema: {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema));
            return objectMapper.writeValueAsBytes(data);

        } catch (IOException | RestClientException e) {
            throw new RuntimeException("Error serializing JSON message", e);
        }
    }

    @Override
    public void close() {
        // Optional: Release resources, if any
    }
}
