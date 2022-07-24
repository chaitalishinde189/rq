package com.example.rqchallenge.employees.model;

import com.example.rqchallenge.employees.util.EmployeeJsonDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

/**
 * @author chaitali shinde
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponse extends BaseResponse {

    @JsonDeserialize(using = EmployeeJsonDeserializer.class)
    private List<Employee> data;

}
