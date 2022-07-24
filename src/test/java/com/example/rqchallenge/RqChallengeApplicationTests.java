package com.example.rqchallenge;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.rqchallenge.employees.constants.ApplicationConstants.EmployeeField;
import static com.example.rqchallenge.employees.constants.MessageConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class RqChallengeApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("Get all employees")
    public void getAllEmployees() {
        ResponseEntity<List<Employee>> responseEntity = testRestTemplate.exchange(getBaseUrl().toString(),
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Employee>>() {
                });
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().size()).isEqualTo(24);
    }

    @Test
    @DisplayName("Get employee with valid id")
    public void getEmployeeByValidId() {
        String validId = "3";
        ResponseEntity<Employee> responseEntity = testRestTemplate.getForEntity(
                getBaseUrl().append("/").append(validId).toString(), Employee.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isEqualTo(3);
    }

    @Test
    @DisplayName("Get employee with invalid id")
    public void getEmployeeByInvalidId() {
        String invalidId = "xx";
        ResponseEntity<ErrorResponse> responseEntity = testRestTemplate.getForEntity(
                getBaseUrl().append("/").append(invalidId).toString(), ErrorResponse.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getMessage())
                .containsIgnoringCase(String.format(EMPLOYEE_NOT_FOUND, invalidId));
    }

    @ParameterizedTest(name = "{index} Get employees with matching name: {0}")
    @CsvSource(value = {
            "ri, 7",
            "Ri, 1",
            "ll, 5",
            "xx, 0"
    })
    @DisplayName("Get employees with matching name")
    public void getEmployeesByNameSearch(String searchString, int expectedEmployeeCount) {
        ResponseEntity<List<Employee>> responseEntity = testRestTemplate.exchange(
                getBaseUrl().append("/search/").append(searchString).toString(),
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Employee>>() {
                });
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().size()).isEqualTo(expectedEmployeeCount);
    }

    @Test
    @DisplayName("Get highest employee salary")
    public void getHighestEmployeeSalary() {
        ResponseEntity<Integer> responseEntity = testRestTemplate.getForEntity(
                getBaseUrl().append("/highestSalary").toString(), Integer.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(725000);
    }

    @Test
    @DisplayName("Get top 10 highest earning employee names")
    public void getTopTenHighestEarningEmployeeNames() {
        ResponseEntity<List<String>> responseEntity = testRestTemplate.exchange(
                getBaseUrl().append("/topTenHighestEarningEmployeeNames").toString(),
                HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
                });
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().size()).isEqualTo(10);
        assertThat(responseEntity.getBody()).containsExactly(
                "Paul Byrd",
                "Yuri Berry",
                "Charde Marshall",
                "Cedric Kelly",
                "Tatyana Fitzpatrick",
                "Brielle Williamson",
                "Jenette Caldwell",
                "Quinn Flynn",
                "Rhona Davidson",
                "Tiger Nixon");
    }

    @Test
    @DisplayName("Create employee with valid data")
    public void createEmployeeWithValidData() {
        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put(EmployeeField.NAME.getValue(), "Chaitali");
        employeeData.put(EmployeeField.AGE.getValue(), "32");
        employeeData.put(EmployeeField.SALARY.getValue(), "10000");

        ResponseEntity<Employee> responseEntity = testRestTemplate.postForEntity(getBaseUrl().toString(),
                employeeData, Employee.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @ParameterizedTest(name = "{index} - {0}")
    @CsvFileSource(resources = "/employee_data.csv")
    @DisplayName("Create employee with invalid data")
    public void createEmployeeWithInvalidData(String testName, String name, String age, String salary) {
        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put(EmployeeField.NAME.getValue(), name);
        employeeData.put(EmployeeField.AGE.getValue(), age);
        employeeData.put(EmployeeField.SALARY.getValue(), salary);

        ResponseEntity<ErrorResponse> responseEntity = testRestTemplate.postForEntity(
                getBaseUrl().toString(), employeeData, ErrorResponse.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody().getMessage()).containsAnyOf(
                String.format(EMPLOYEE_VALIDATION_MISSING_FIELD_VALUE_ERROR, EmployeeField.NAME.getValue()),
                String.format(EMPLOYEE_VALIDATION_MISSING_FIELD_VALUE_ERROR, EmployeeField.AGE.getValue()),
                String.format(EMPLOYEE_VALIDATION_MISSING_FIELD_VALUE_ERROR, EmployeeField.SALARY.getValue()),
                String.format(EMPLOYEE_VALIDATION_INVALID_FIELD_VALUE_ERROR, EmployeeField.NAME.getValue()),
                String.format(EMPLOYEE_VALIDATION_INVALID_FIELD_VALUE_ERROR, EmployeeField.AGE.getValue()),
                String.format(EMPLOYEE_VALIDATION_INVALID_FIELD_VALUE_ERROR, EmployeeField.SALARY.getValue())
        );
    }

    @Test
    @DisplayName("Delete employee with valid id")
    public void deleteEmployeeByValidId() {
        String validId = "3";
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                getBaseUrl().append("/").append(validId).toString(), HttpMethod.DELETE, null, String.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo("Ashton Cox");
    }

    @Test
    @DisplayName("Delete employee with invalid id")
    public void deleteEmployeeByInvalidId() {
        String invalidId = "xx";
        ResponseEntity<ErrorResponse> responseEntity = testRestTemplate.exchange(
                getBaseUrl().append("/").append(invalidId).toString(), HttpMethod.DELETE, null,
                ErrorResponse.class);
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getMessage())
                .containsIgnoringCase(String.format(EMPLOYEE_NOT_FOUND, invalidId));
    }

    private StringBuilder getBaseUrl() {
        StringBuilder baseUrl = new StringBuilder("http://localhost:");
        return baseUrl.append(port).append("/employees");
    }

}
