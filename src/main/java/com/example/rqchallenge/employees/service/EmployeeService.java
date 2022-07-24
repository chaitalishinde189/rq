package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.model.Employee;

import java.util.List;
import java.util.Map;

/**
 * @author chaitali shinde
 */
public interface EmployeeService {
    List<Employee> getAllEmployees();

    List<Employee> getEmployeesByNameSearch(String searchString);

    Employee getEmployeeById(String id);

    Integer getHighestSalaryOfEmployees();

    List<String> getTopNHighestEarningEmployeeNames(int limit);

    Employee createEmployee(Map<String, Object> employeeData);

    String deleteEmployeeById(String id);
}
