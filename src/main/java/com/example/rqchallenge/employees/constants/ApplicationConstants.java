package com.example.rqchallenge.employees.constants;

import lombok.Getter;

/**
 * @author chaitali shinde
 */
public interface ApplicationConstants {

    enum ResponseStatus {
        SUCCESS("success"), FAILURE("failure");
        @Getter
        final String value;

        ResponseStatus(String value) {
            this.value = value;
        }
    }

    enum InputType {
        STRING, INTEGER
    }

    enum EmployeeField {
        NAME("name"), AGE("age"), SALARY("salary");
        @Getter
        final String value;

        EmployeeField(String value) {
            this.value = value;
        }
    }

}
