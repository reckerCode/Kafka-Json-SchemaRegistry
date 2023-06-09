package com.kafka.poc.sr.domain;

import com.kafka.poc.sr.domain.enums.EmployeeType;
import lombok.Data;

@Data

public class Employee {

    //    @JsonProperty(required = true)
    Address address;

    //    @JsonProperty(required = true)
    User user;

    //    @JsonProperty(required = true)
    EmployeeType employeeType;

    //    @JsonProperty(required = true)
    String employeeType2;
}
