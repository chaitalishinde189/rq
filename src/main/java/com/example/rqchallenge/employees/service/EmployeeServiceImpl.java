package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.annotation.LogExecutionTime;
import com.example.rqchallenge.employees.constants.ApplicationConstants;
import com.example.rqchallenge.employees.exception.EmployeeServiceException;
import com.example.rqchallenge.employees.exception.InvalidRequestException;
import com.example.rqchallenge.employees.exception.ResourceNotFoundException;
import com.example.rqchallenge.employees.helper.EmployeeServiceHelper;
import com.example.rqchallenge.employees.helper.EmployeeValidator;
import com.example.rqchallenge.employees.model.BaseResponse;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.model.EmployeeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.rqchallenge.employees.constants.MessageConstants.*;

/**
 * @author chaitali shinde
 * Service class performing business logic for employee operations
 */
@Service
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeServiceHelper employeeServiceHelper;
    private final EmployeeValidator employeeValidator;

    /**
     * Get all employees
     *
     * @return List<Employee>
     */
    @LogExecutionTime
    @Override
    public List<Employee> getAllEmployees() {
        try {
            List<Employee> employees = employeeServiceHelper.getAllEmployees().getData();
            if (CollectionUtils.isEmpty(employees)) {
                return Collections.emptyList();
            }
            return employees;
        } catch (Exception e) {
            log.error("Error while fetching list of employees", e);
            throw new EmployeeServiceException(GET_ALL_EMPLOYEE_ERROR);
        }
    }

    /**
     * Get employees whose name matches the given search string
     *
     * @param searchString
     * @return List<Employee>
     */
    @LogExecutionTime
    @Override
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        try {
            List<Employee> employees = employeeServiceHelper.getAllEmployees().getData();
            if (CollectionUtils.isEmpty(employees)) {
                return Collections.emptyList();
            }
            return employees.stream()
                    .filter(employee -> employee.getName().contains(searchString))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error while fetching list of employees with name matching with: {}", searchString, e);
            throw new EmployeeServiceException(String.format(GET_EMPLOYEES_NAME_SEARCH_ERROR, searchString));
        }
    }

    /**
     * Get employee by id
     *
     * @param id
     * @return Employee
     */
    @LogExecutionTime
    @Override
    public Employee getEmployeeById(String id) {
        try {
            List<Employee> employees = employeeServiceHelper.getEmployeeById(id).getData();
            if (CollectionUtils.isEmpty(employees)) {
                throw new ResourceNotFoundException(String.format(EMPLOYEE_NOT_FOUND, id));
            }
            return employees.get(0);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching employee with id: {}", id, e);
            throw e;
        }
    }

    /**
     * Get the highest salary of employees
     *
     * @return Integer
     */
    @LogExecutionTime
    @Override
    public Integer getHighestSalaryOfEmployees() {
        try {
            List<Employee> employees = employeeServiceHelper.getAllEmployees().getData();
            if (!CollectionUtils.isEmpty(employees)) {
                return employees.stream()
                        .max(Comparator.comparing(Employee::getSalary))
                        .map(Employee::getSalary)
                        .orElse(0);
            }
            return 0;
        } catch (Exception e) {
            log.error("Error while fetching highest salary of employees", e);
            throw new EmployeeServiceException(GET_EMPLOYEE_HIGHEST_SALARY_ERROR);
        }
    }

    /**
     * Get top N employee names with the highest earnings
     *
     * @return List<String>
     */
    @LogExecutionTime
    @Override
    public List<String> getTopNHighestEarningEmployeeNames(int limit) {
        try {
            List<Employee> employees = employeeServiceHelper.getAllEmployees().getData();
            if (!CollectionUtils.isEmpty(employees)) {
                return employees.stream()
                        .sorted(Collections.reverseOrder(Comparator.comparing(Employee::getSalary)))
                        .limit(limit)
                        .map(Employee::getName)
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Error while fetching top {} highest earning employee names", limit, e);
            throw new EmployeeServiceException(GET_EMPLOYEE_NAMES_HIGHEST_EARNINGS_ERROR);
        }
    }

    /**
     * Create employee with provided data
     *
     * @param employeeData
     * @return Employee
     */
    @LogExecutionTime
    @Override
    public Employee createEmployee(Map<String, Object> employeeData) {
        try {
            employeeValidator.validateEmployee(employeeData);
            EmployeeResponse response = employeeServiceHelper.createEmployee(employeeData);
            if (response.getStatus().equals(ApplicationConstants.ResponseStatus.SUCCESS.getValue())
                    && !CollectionUtils.isEmpty(response.getData())) {
                return response.getData().get(0);
            }
            throw new EmployeeServiceException(EMPLOYEE_NOT_ADDED_ERROR);
        } catch (InvalidRequestException e) {
            log.error("Error while creating the employee with name: {}",
                    employeeData.get(ApplicationConstants.EmployeeField.NAME.getValue()), e);
            throw e;
        } catch (Exception e) {
            log.error("Error while creating the employee with name: {}",
                    employeeData.get(ApplicationConstants.EmployeeField.NAME.getValue()), e);
            throw new EmployeeServiceException(EMPLOYEE_CREATION_ERROR);
        }
    }

    /**
     * Deletes employee with provided id
     *
     * @param id
     * @return String (name of employee which is deleted)
     */
    @LogExecutionTime
    @Override
    public String deleteEmployeeById(String id) {
        try {
            Employee employee = getEmployeeById(id);
            BaseResponse response = employeeServiceHelper.deleteEmployee(id);
            if (response.getStatus().equals(ApplicationConstants.ResponseStatus.SUCCESS.getValue())) {
                return employee.getName();
            }
            throw new EmployeeServiceException(EMPLOYEE_NOT_REMOVED_ERROR);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error while deleting the employee with id: {}", id, e);
            throw new EmployeeServiceException(String.format(EMPLOYEE_DELETION_ERROR, id));
        }
    }

}
