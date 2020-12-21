package br.com.sicredi.backendtest.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@Document
public class Vote {

    @Id
    private String id;
    private String discussionId;
    private String cpf;
    private Boolean vote;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime created;

}
