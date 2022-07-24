package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.model.BaseResponse;
import com.example.rqchallenge.employees.model.EmployeeResponse;

import java.util.Map;

/**
 * @author chaitali shinde
 * API contract for employee operations with third party
 */
public interface EmployeeSourceService {

    EmployeeResponse getAllEmployees();

    EmployeeResponse getEmployeeById(String id);

    EmployeeResponse createEmployee(Map<String, Object> employeeData);

    BaseResponse deleteEmployee(String id);

}
