package br.com.sicredi.backendtest.exception;

import org.springframework.http.HttpStatus;

public class ExpectationException extends ApiException {

    private HttpStatus httpStatus;

    public ExpectationException(String message) {
        super(HttpStatus.EXPECTATION_FAILED, message);
    }

}
