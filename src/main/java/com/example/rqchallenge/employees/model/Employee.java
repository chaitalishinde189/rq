package com.example.rqchallenge.employees.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author chaitali shinde
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Employee {

    private int id;

    @JsonAlias({"employee_name"})
    private String name;

    @JsonAlias({"employee_age"})
    private int age;

    @JsonAlias({"employee_salary"})
    private int salary;

    private String profileImage;

}
