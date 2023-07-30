package com.kafka.poc.sr.service;

import com.kafka.poc.sr.domain.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ListenerService {

    //    @KafkaListener(containerFactory = "kafkaListenerContainerFactory",
//            topics = "ppm-test-topic3",
//            clientIdPrefix = "test1",
//            groupId = "employeeRecordSubscriber")
    public void kafkaListenerService(ConsumerRecord<String, Employee> record) {
        log.info("Consumer record headers: {}", record.headers());
        log.info("Consumer record value: {}", record.value());
        log.info("Consumer record timestamp: {}", record.timestamp());

    }
}
