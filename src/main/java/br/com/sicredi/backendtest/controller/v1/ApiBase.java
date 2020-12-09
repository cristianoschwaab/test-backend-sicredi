package br.com.sicredi.backendtest.controller.v1;

import br.com.sicredi.backendtest.controller.v1.model.ErrorResponse;
import br.com.sicredi.backendtest.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidatorAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.*;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public abstract class ApiBase implements Serializable {

    @Autowired
    private Validator validator;

    @ExceptionHandler({ ApiException.class })
    public Mono<ResponseEntity<ErrorResponse>> handleApiException(ApiException apiException) {
        final ErrorResponse<?> response = new ErrorResponse<Serializable>(apiException.getMessage());
        log.error("Handler error response  {}", response);
        return Mono.just(ResponseEntity.status(apiException.getHttpStatus()).body(response));
    }

    @ExceptionHandler({ Exception.class })
    public Mono<ResponseEntity<ErrorResponse>> handleException(Exception e) {
        final ErrorResponse<?> response = new ErrorResponse<Serializable>(e.getMessage());
        log.error("Unhandler error response {}", response);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
    }

}
