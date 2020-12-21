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
@Builder
@EqualsAndHashCode
@Document
public class Discussion {

    @Id
    private String id;
    private String title;
    private String description;
    private String cpfCreator;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime created;

}
