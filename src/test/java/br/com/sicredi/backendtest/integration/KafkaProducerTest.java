package br.com.sicredi.backendtest.integration;

import br.com.sicredi.backendtest.entity.Summary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.ListenableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ListenableFuture callBack;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Summary summary;

    @InjectMocks
    private KafkaProducer kafkaProducer;

    @Test
    void shouldSuccessWhenSendKafkaMessage() throws JsonProcessingException {
        ReflectionTestUtils.setField(kafkaProducer, "topic", "topicTest");
        given(kafkaTemplate.send(any(), anyString())).willReturn(callBack);
        given(objectMapper.writeValueAsString(summary)).willReturn("{\"teste\":\"teste\"}");
        kafkaProducer.sendMessage(summary);
        verify(kafkaTemplate, times(1)).send(anyString(), anyString());
    }

    @Test
    void shouldErrorWhenSendKafkaMessage() throws JsonProcessingException {
        ReflectionTestUtils.setField(kafkaProducer, "topic", "topicTest");
        given(objectMapper.writeValueAsString(summary)).willThrow(new RuntimeException("error"));
        Assertions.assertThrows(RuntimeException.class, () -> {
            kafkaProducer.sendMessage(summary);
        });
    }

}