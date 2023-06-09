package com.kafka.poc.sr.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {

    //  @JsonProperty(required = true)
    public String firstName;

    //  @JsonProperty(required = true)
    public String lastName;

    //  @JsonProperty(required = true)
    public int age;

}