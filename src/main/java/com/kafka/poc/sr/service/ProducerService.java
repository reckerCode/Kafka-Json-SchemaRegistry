package com.kafka.poc.sr.service;

import com.kafka.poc.sr.domain.Address;
import com.kafka.poc.sr.domain.Employee;
import com.kafka.poc.sr.domain.User;
import com.kafka.poc.sr.domain.enums.EmployeeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class ProducerService {
    @Qualifier("getEmployeeKafkaTemplate")
    @Autowired
    private KafkaTemplate<String, Employee> stringEmployeeKafkaTemplate;

    public void testServiceClass() throws ExecutionException, InterruptedException {
        Employee employee = new Employee();
        employee.setEmployeeType(EmployeeType.JUNIOR);
        employee.setEmployeeType2("Test");
        User user = new User();
        user.setAge(5);
//        user.setLastName("Test");
//        user.setFirstName("test");
        employee.setUser(user);
        Address address = new Address();
        address.setCity("testCity");
        address.setCountry("TestCountry");
        employee.setAddress(address);

        String schemaId = "3";
        ProducerRecord<String, Employee> producerRecord = new ProducerRecord<>("ppm-test-topic3", employee);
//        producerRecord.headers().add("schemaId", schemaId.getBytes());
        SendResult<String, Employee> stringEmployeeSendResult = stringEmployeeKafkaTemplate.send(producerRecord).get();
        log.info("Timestamp: {}", stringEmployeeSendResult.getRecordMetadata().timestamp());
        log.info("Partition: {}", stringEmployeeSendResult.getRecordMetadata().partition());
        log.info("Topic: {}", stringEmployeeSendResult.getRecordMetadata().topic());
        log.info("serializedValueSize: {}", stringEmployeeSendResult.getRecordMetadata().serializedValueSize());
    }
}
