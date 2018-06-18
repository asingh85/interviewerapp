package com.softvision.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecruiterNotFoundException extends RuntimeException {
    public RecruiterNotFoundException(Exception exception) {
        super(exception);
    }
}
