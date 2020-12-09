package br.com.sicredi.backendtest.controller.v1.model;

import lombok.Data;
import java.io.Serializable;

@Data
public class VoteRequest implements Serializable {
    private String cpf;
    private Boolean vote;
}
