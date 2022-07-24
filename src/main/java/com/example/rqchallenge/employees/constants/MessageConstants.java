package com.example.rqchallenge.employees.constants;

/**
 * @author chaitali shinde
 * Contains API response messages
 */
public interface MessageConstants {

    String GENERIC_ERROR_MESSAGE = "Something went wrong, please try again later.";
    String GET_ALL_EMPLOYEE_ERROR = "Error occurred while fetching all employees";
    String GET_EMPLOYEES_NAME_SEARCH_ERROR = "Error occurred while fetching employees having name matching with '%s'";
    String GET_EMPLOYEE_HIGHEST_SALARY_ERROR = "Error occurred while fetching highest salary of employees";
    String GET_EMPLOYEE_NAMES_HIGHEST_EARNINGS_ERROR = "Error occurred while fetching top 10 highest earning employee names";
    String EMPLOYEE_NOT_FOUND = "Employee with id %s not found";
    String EMPLOYEE_VALIDATION_MISSING_FIELD_VALUE_ERROR = "Employee data is not valid. Missing value for field '%s'.";
    String EMPLOYEE_VALIDATION_INVALID_FIELD_VALUE_ERROR = "Employee data is not valid. Invalid value for field '%s'.";
    String EMPLOYEE_NOT_ADDED_ERROR = "Employee not added to system";
    String EMPLOYEE_CREATION_ERROR = "Error occurred while creating the employee";
    String EMPLOYEE_NOT_REMOVED_ERROR = "Employee not removed from system";
    String EMPLOYEE_DELETION_ERROR = "Error occurred while deleting the employee with id %s";
}
