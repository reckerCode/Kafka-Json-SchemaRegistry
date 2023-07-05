package com.kafka.poc.sr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.kafka.poc.sr.domain.Developer;
import com.kafka.poc.sr.domain.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
@Slf4j
public class SrApplication {

    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(SrApplication.class, args);
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator generator = new JsonSchemaGenerator(mapper);
        JsonSchema jsonSchema = generator.generateSchema(Developer.class);
        log.info("Schema: {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema));

    }

}
