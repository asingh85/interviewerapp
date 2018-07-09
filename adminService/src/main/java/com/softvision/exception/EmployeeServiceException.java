package com.softvision.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeServiceException extends RuntimeException {
    public EmployeeServiceException(String exception) {
        super(exception);
    }

}
