package br.com.sicredi.backendtest.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {

    private HttpStatus httpStatus;

    public UnauthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

}
