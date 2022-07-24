package com.example.rqchallenge.employees.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author chaitali shinde
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EmployeeServiceException extends RuntimeException {

    public EmployeeServiceException(String message) {
        super(message);
    }

}
