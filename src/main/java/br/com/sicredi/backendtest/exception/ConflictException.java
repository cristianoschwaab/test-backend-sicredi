package br.com.sicredi.backendtest.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends ApiException {

    private HttpStatus httpStatus;

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

}
