package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.model.BaseResponse;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.model.EmployeeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.util.Map;
import java.util.stream.Collectors;

import static com.example.rqchallenge.employees.constants.ApplicationConstants.EmployeeField;

/**
 * @author chaitali shinde
 * Class providing mock response for employee operations
 */
@Service("employeeSourceMockService")
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeSourceMockServiceImpl implements EmployeeSourceService {

    private final ObjectMapper objectMapper;

    private static final String GET_EMPLOYEES_JSON_RESPONSE_FILE = "employees.json";
    private static final String CREATE_EMPLOYEE_JSON_RESPONSE_FILE = "create_employee.json";
    private static final String DELETE_EMPLOYEE_JSON_RESPONSE_FILE = "delete_employee.json";

    @SneakyThrows
    @Override
    public EmployeeResponse getAllEmployees() {
        log.info("Fetching all employees using mock");
        return objectMapper.readValue(ResourceUtils.getFile("classpath:" + GET_EMPLOYEES_JSON_RESPONSE_FILE), EmployeeResponse.class);
    }

    @SneakyThrows
    @Override
    public EmployeeResponse getEmployeeById(String id) {
        log.info("Fetching employee with id: {} using mock", id);
        EmployeeResponse employeeResponse = getAllEmployees();
        employeeResponse.setData(employeeResponse.getData().stream()
                .filter(employee -> String.valueOf(employee.getId()).equals(id))
                .collect(Collectors.toList()));
        return employeeResponse;
    }

    @SneakyThrows
    @Override
    public EmployeeResponse createEmployee(Map<String, Object> employeeData) {
        log.info("Creating employee with name: {} using mock", employeeData.get(EmployeeField.NAME.getValue()));
        EmployeeResponse employeeResponse = objectMapper.readValue(ResourceUtils.getFile("classpath:" + CREATE_EMPLOYEE_JSON_RESPONSE_FILE),
                EmployeeResponse.class);
        Employee employee = employeeResponse.getData().get(0);
        employee.setName(employeeData.get(EmployeeField.NAME.getValue()).toString());
        employee.setAge(Integer.parseInt(employeeData.get(EmployeeField.AGE.getValue()).toString()));
        employee.setSalary(Integer.parseInt(employeeData.get(EmployeeField.SALARY.getValue()).toString()));
        return employeeResponse;
    }

    @SneakyThrows
    @Override
    public BaseResponse deleteEmployee(String id) {
        log.info("Deleting employee with id: {} using mock", id);
        return objectMapper.readValue(ResourceUtils.getFile("classpath:" + DELETE_EMPLOYEE_JSON_RESPONSE_FILE),
                BaseResponse.class);
    }

}
