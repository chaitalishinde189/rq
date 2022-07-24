package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.constants.ApplicationConstants;
import com.example.rqchallenge.employees.exception.EmployeeSourceServiceException;
import com.example.rqchallenge.employees.model.BaseResponse;
import com.example.rqchallenge.employees.model.EmployeeResponse;
import com.example.rqchallenge.employees.util.RetryableRestClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.Map;

/**
 * @author chaitali shinde
 * Class fetching response for employee operations from third party APIs
 */
@Service
@Primary
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeSourceAPIServiceImpl implements EmployeeSourceService {

    private final RetryableRestClient restClient;

    private static final String BASE_ENDPOINT = "https://dummy.restapiexample.com/api/v1";
    private static final String GET_ALL_EMPLOYEES = BASE_ENDPOINT + "/employees";
    private static final String GET_EMPLOYEE_BY_ID = BASE_ENDPOINT + "/employee/%s";
    private static final String CREATE_EMPLOYEE = BASE_ENDPOINT + "/create";
    private static final String DELETE_EMPLOYEE_BY_ID = BASE_ENDPOINT + "/delete/%s";

    @Override
    public EmployeeResponse getAllEmployees() {
        try {
            log.info("Fetching all employees");
            return restClient.exchange(GET_ALL_EMPLOYEES, HttpMethod.GET,
                    new HttpEntity<>(getHttpHeaders()), EmployeeResponse.class).getBody();
        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new EmployeeSourceServiceException(e);
        }
    }

    @Override
    public EmployeeResponse getEmployeeById(String id) {
        try {
            log.info("Fetching employee with id: {}", id);
            return restClient.exchange(String.format(GET_EMPLOYEE_BY_ID, id),
                    HttpMethod.GET, new HttpEntity<>(getHttpHeaders()), EmployeeResponse.class).getBody();
        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new EmployeeSourceServiceException(e);
        }
    }

    @Override
    public EmployeeResponse createEmployee(Map<String, Object> employeeData) {
        try {
            log.info("Creating employee with name: {}", employeeData.get(ApplicationConstants.EmployeeField.NAME.getValue()));
            return restClient.exchange(CREATE_EMPLOYEE, HttpMethod.POST, new HttpEntity<>(employeeData, getHttpHeaders()),
                    EmployeeResponse.class).getBody();
        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new EmployeeSourceServiceException(e);
        }
    }

    @Override
    public BaseResponse deleteEmployee(String id) {
        try {
            log.info("Deleting employee with id: {}", id);
            return restClient.exchange(String.format(DELETE_EMPLOYEE_BY_ID, id),
                    HttpMethod.DELETE, new HttpEntity<>(getHttpHeaders()), BaseResponse.class).getBody();
        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new EmployeeSourceServiceException(e);
        }
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

}
