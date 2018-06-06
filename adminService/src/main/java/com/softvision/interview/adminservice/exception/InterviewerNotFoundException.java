package com.softvision.interview.adminservice.exception;

import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InterviewerNotFoundException  extends RuntimeException{
    public InterviewerNotFoundException(Exception exception){
        super(exception);
    }
}
