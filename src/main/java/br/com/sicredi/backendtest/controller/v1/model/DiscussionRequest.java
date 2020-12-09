package br.com.sicredi.backendtest.controller.v1.model;

import lombok.Data;
import java.io.Serializable;

@Data
public class DiscussionRequest implements Serializable {
    private String title;
    private String description;
    private String cpfCreator;
}
