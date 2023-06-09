package com.kafka.poc.sr.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Address {

    //    @JsonProperty(required = true)
    String city;

    //    @JsonProperty(required = true)
    String country;

    //    @JsonProperty(required = true)
    String streetNo;

}
