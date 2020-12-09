package br.com.sicredi.backendtest.controller.v1.model;

import lombok.Data;
import java.io.Serializable;

@Data
public class SessionRequest implements Serializable {
    private Long timer;
    private String cpfCreator;
}
