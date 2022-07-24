package com.example.rqchallenge.employees.helper;

import com.example.rqchallenge.employees.exception.InvalidRequestException;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.rqchallenge.employees.constants.ApplicationConstants.EmployeeField;
import static com.example.rqchallenge.employees.constants.ApplicationConstants.InputType;
import static com.example.rqchallenge.employees.constants.MessageConstants.EMPLOYEE_VALIDATION_INVALID_FIELD_VALUE_ERROR;
import static com.example.rqchallenge.employees.constants.MessageConstants.EMPLOYEE_VALIDATION_MISSING_FIELD_VALUE_ERROR;

/**
 * @author chaitali shinde
 * Helper class validating employee data
 */
@Component
public class EmployeeValidator {
    private static final Map<EmployeeField, InputType> EMPLOYEE_FIELD_VS_TYPE_MAP = Stream.of(
                    new AbstractMap.SimpleImmutableEntry<>(EmployeeField.NAME, InputType.STRING),
                    new AbstractMap.SimpleImmutableEntry<>(EmployeeField.AGE, InputType.INTEGER),
                    new AbstractMap.SimpleImmutableEntry<>(EmployeeField.SALARY, InputType.INTEGER))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    public void validateEmployee(Map<String, Object> employeeData) {
        EMPLOYEE_FIELD_VS_TYPE_MAP.forEach((field, inputType) -> {
            if (!employeeData.containsKey(field.getValue())
                    || Objects.isNull(employeeData.get(field.getValue()))) {
                throw new InvalidRequestException(String.format(EMPLOYEE_VALIDATION_MISSING_FIELD_VALUE_ERROR, field.getValue()));
            }
            if (String.valueOf(employeeData.get(field.getValue())).trim().isEmpty()) {
                throw new InvalidRequestException(String.format(EMPLOYEE_VALIDATION_INVALID_FIELD_VALUE_ERROR, field.getValue()));
            }
            validateFieldInputType(field, employeeData.get(field.getValue()));
        });
    }

    private void validateFieldInputType(EmployeeField field, Object fieldValue) {
        InputType inputType = EMPLOYEE_FIELD_VS_TYPE_MAP.get(field);
        if (inputType.equals(InputType.INTEGER)) {
            validateIntegerFieldValue(field, fieldValue);
        }
    }

    private void validateIntegerFieldValue(EmployeeField field, Object fieldValue) {
        try {
            Integer.parseInt(fieldValue.toString());
        } catch (NumberFormatException e) {
            throw new InvalidRequestException(String.format(EMPLOYEE_VALIDATION_INVALID_FIELD_VALUE_ERROR, field.getValue()));
        }
    }

}
