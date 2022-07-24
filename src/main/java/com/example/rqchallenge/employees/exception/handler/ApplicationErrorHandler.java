package com.example.rqchallenge.employees.exception.handler;

import com.example.rqchallenge.employees.exception.EmployeeServiceException;
import com.example.rqchallenge.employees.exception.InvalidRequestException;
import com.example.rqchallenge.employees.exception.ResourceNotFoundException;
import com.example.rqchallenge.employees.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.rqchallenge.employees.constants.MessageConstants.GENERIC_ERROR_MESSAGE;
import static org.springframework.http.HttpStatus.*;

/**
 * @author chaitali shinde
 * Custom error handler for application exceptions
 */
@RestControllerAdvice
public class ApplicationErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(GENERIC_ERROR_MESSAGE, INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmployeeServiceException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeServiceException(EmployeeServiceException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), NOT_FOUND);
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), BAD_REQUEST);
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

}
