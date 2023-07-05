package com.kafka.poc.sr.domain;

import lombok.Data;

@Data
public class Developer<T> {
    DeveloperMetadata developerMetadata;
    T developerDetailPayload;
}
