package br.com.sicredi.backendtest.controller.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class ErrorResponse<T> implements Serializable {
    private String message;
    private List<String> errors;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(List<String> errors) {
        this.errors = errors;
    }
}
