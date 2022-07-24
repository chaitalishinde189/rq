package com.example.rqchallenge.employees.helper;

import com.example.rqchallenge.employees.exception.EmployeeSourceServiceException;
import com.example.rqchallenge.employees.model.BaseResponse;
import com.example.rqchallenge.employees.model.EmployeeResponse;
import com.example.rqchallenge.employees.service.EmployeeSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author chaitali shinde
 * Helper class abstracting employee operations response - hiding if response is mocked or from API
 */
@Component
@Slf4j
public class EmployeeServiceHelper implements EmployeeSourceService {

    private final EmployeeSourceService employeeSourceService;
    private final EmployeeSourceService employeeSourceMockService;

    @Autowired
    public EmployeeServiceHelper(EmployeeSourceService employeeSourceService,
                                 @Qualifier("employeeSourceMockService") EmployeeSourceService employeeSourceMockService) {
        this.employeeSourceService = employeeSourceService;
        this.employeeSourceMockService = employeeSourceMockService;
    }

    @Retryable(value = {EmployeeSourceServiceException.class}, maxAttempts = 1, recover = "getAllEmployeesMock")
    @Override
    public EmployeeResponse getAllEmployees() {
        return employeeSourceService.getAllEmployees();
    }

    @Recover
    public EmployeeResponse getAllEmployeesMock(EmployeeSourceServiceException e) {
        return employeeSourceMockService.getAllEmployees();
    }

    @Retryable(value = {EmployeeSourceServiceException.class}, maxAttempts = 1, recover = "getEmployeeByIdMock")
    @Override
    public EmployeeResponse getEmployeeById(String id) {
        return employeeSourceService.getEmployeeById(id);
    }

    @Recover
    public EmployeeResponse getEmployeeByIdMock(EmployeeSourceServiceException e, String id) {
        return employeeSourceMockService.getEmployeeById(id);
    }

    @Retryable(value = {EmployeeSourceServiceException.class}, maxAttempts = 1, recover = "createEmployeeMock")
    @Override
    public EmployeeResponse createEmployee(Map<String, Object> employeeData) {
        return employeeSourceService.createEmployee(employeeData);
    }

    @Recover
    public EmployeeResponse createEmployeeMock(EmployeeSourceServiceException e, Map<String, Object> employeeData) {
        return employeeSourceMockService.createEmployee(employeeData);
    }

    @Retryable(value = {EmployeeSourceServiceException.class}, maxAttempts = 1, recover = "deleteEmployeeMock")
    @Override
    public BaseResponse deleteEmployee(String id) {
        return employeeSourceService.deleteEmployee(id);
    }

    @Recover
    public BaseResponse deleteEmployeeMock(EmployeeSourceServiceException e, String id) {
        return employeeSourceMockService.deleteEmployee(id);
    }

}
