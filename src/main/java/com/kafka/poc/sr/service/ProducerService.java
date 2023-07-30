package com.kafka.poc.sr.service;

import com.kafka.poc.sr.domain.Developer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class ProducerService {
    @Qualifier("getEmployeeKafkaTemplate")
    @Autowired
    private KafkaTemplate<String, Developer> stringEmployeeKafkaTemplate;

    public void testServiceClass(Developer employee) throws ExecutionException, InterruptedException {
        try {
            String string = UUID.randomUUID().toString();
            ProducerRecord<String, Developer> producerRecord = new ProducerRecord<>("test-topic-221", employee);
            SendResult<String, Developer> stringEmployeeSendResult = stringEmployeeKafkaTemplate.send(producerRecord).get();
            log.info("Timestamp: {}", stringEmployeeSendResult.getRecordMetadata().timestamp());
            log.info("Partition: {}", stringEmployeeSendResult.getRecordMetadata().partition());
            log.info("Topic: {}", stringEmployeeSendResult.getRecordMetadata().topic());
            log.info("serializedValueSize: {}", stringEmployeeSendResult.getRecordMetadata().serializedValueSize());
        } catch (ExecutionException e) {
            log.info("Class: {}", e.getClass());
            log.info("Message: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
