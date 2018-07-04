package com.softvision.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author arun.p
 * <p>
 * The Class CandidateNotFoundException.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CandidateNotFoundException extends RuntimeException {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new candidate not found exception.
     *
     * @param exception the exception
     */
    public CandidateNotFoundException(Exception exception) {
        super(exception);
    }
}
