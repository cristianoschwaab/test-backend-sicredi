package br.com.sicredi.backendtest.integration;

import br.com.sicredi.backendtest.integration.model.UsersResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UsersClient {

    @Value("${app.client.user.url}")
    private String url;

    @Value("${app.client.user.path}")
    private String path;

    public Mono<ResponseEntity<UsersResponse>> findVoteCredentials(String cpf) {

        WebClient webClient = WebClient
                .builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        WebClient.ResponseSpec response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path(path + "/{cpf}")
                    .build(cpf)
                )
                .retrieve();

        return response.toEntity(UsersResponse.class);
    }

}
