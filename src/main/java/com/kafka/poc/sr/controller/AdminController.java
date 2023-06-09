package com.kafka.poc.sr.controller;

import com.kafka.poc.sr.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class AdminController {


    @Autowired
    ProducerService serviceClass;

    @GetMapping("/test")
    public void testServiceClassController() throws ExecutionException, InterruptedException {
        serviceClass.testServiceClass();
    }
}
