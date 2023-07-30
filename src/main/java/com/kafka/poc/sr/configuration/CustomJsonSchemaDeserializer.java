package com.kafka.poc.sr.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.schemaregistry.json.JsonSchemaProvider;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

public class CustomJsonSchemaDeserializer<T> implements Deserializer<T> {

    private ObjectMapper objectMapper;
    @Autowired
    private SchemaRegistryClient schemaRegistryClient;
    private Class<T> type;

    public CustomJsonSchemaDeserializer(Class<T> type) {
        this.type = type;
    }


    public Map<String, Object> getSchemaRegistryConfig() {
        Map<String, Object> map = new HashMap();
        map.put(KafkaJsonSchemaDeserializerConfig.AUTO_REGISTER_SCHEMAS, false);
        map.put(KafkaJsonSchemaDeserializerConfig.USE_LATEST_VERSION, true);
        map.put(KafkaJsonSchemaDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
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
    public T deserialize(String topic, byte[] bytes) {
        return deserialize(topic, null, bytes);
    }

    @Override
    public T deserialize(String topic, Headers headers, byte[] bytes) {
        try {
            // Fetch the latest version of the schema from the Schema Registry
            String schema = schemaRegistryClient.getLatestSchemaMetadata(topic + "-value").getSchema();

            // Validate object against schema
            com.networknt.schema.JsonSchema validatorSchema = JsonSchemaFactory.getInstance().getSchema(schema);
            JsonNode objectJsonNode = objectMapper.readTree(bytes);
            Set<ValidationMessage> validationResult = validatorSchema.validate(objectJsonNode);

            // Throw an exception if the object is not valid
            if (!validationResult.isEmpty()) {
                throw new RuntimeException("Object failed JSON schema validation: " + validationResult.toString());
            }

            // Deserialize the JSON byte array into an object
            return objectMapper.treeToValue(objectJsonNode, type);

        } catch (IOException | RestClientException e) {
            throw new RuntimeException("Error deserializing JSON message", e);
        }
    }

    @Override
    public void close() {
        // Optional: Release resources, if any
    }
}