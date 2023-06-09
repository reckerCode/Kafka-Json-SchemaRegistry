package com.kafka.poc.sr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SrApplication {

    public static void main(String[] args) {
        SpringApplication.run(SrApplication.class, args);
    }

}
