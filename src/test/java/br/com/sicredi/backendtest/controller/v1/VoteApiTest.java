package br.com.sicredi.backendtest.controller.v1;

import br.com.sicredi.backendtest.controller.v1.fixture.VoteRequestFixture;
import br.com.sicredi.backendtest.controller.v1.model.VoteRequest;
import br.com.sicredi.backendtest.entity.Vote;
import br.com.sicredi.backendtest.exception.ConflictException;
import br.com.sicredi.backendtest.exception.ExpectationException;
import br.com.sicredi.backendtest.service.VoteService;
import br.com.sicredi.backendtest.service.fixture.VoteFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest(VoteApi.class)
class VoteApiTest {

        @MockBean
        private VoteService voteService;

        @MockBean
        private ModelMapper mapper;

        @Autowired
        private WebTestClient webClient;

    @Test
    void ShouldSuccessWhenCreated() {

        VoteRequest voteRequest = VoteRequestFixture.getVoteRequest();
        Vote vote = VoteFixture.getFarovableVote();

        given(voteService.createVote(anyString(), any())).willReturn(Mono.just(vote));
        given(mapper.map(voteRequest, Vote.class)).willReturn(vote);

        webClient.post()
                .uri("/v1/vote/testeId")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(voteRequest), VoteRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void shouldErrorWhenConflict() {

        VoteRequest voteRequest = VoteRequestFixture.getVoteRequest();
        Vote vote = VoteFixture.getFarovableVote();

        given(voteService.createVote(anyString(), any())).willReturn(
                Mono.error(new ConflictException("Conflict.")));
        given(mapper.map(voteRequest, Vote.class)).willReturn(vote);

        webClient.post()
                .uri("/v1/vote/testeId")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(voteRequest), VoteRequest.class)
                .exchange()
                .expectStatus()
                .isEqualTo(409);
    }

    @Test
    void shouldErrorWhenExpectationFailed() {

        VoteRequest voteRequest = VoteRequestFixture.getVoteRequest();
        Vote vote = VoteFixture.getFarovableVote();

        given(voteService.createVote(anyString(), any())).willReturn(
                Mono.error(new ExpectationException("Expectation Failed.")));
        given(mapper.map(voteRequest, Vote.class)).willReturn(vote);

        webClient.post()
                .uri("/v1/vote/testeId")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(voteRequest), VoteRequest.class)
                .exchange()
                .expectStatus()
                .isEqualTo(417);
    }

    @Test
    void shouldErrorWhenInternalServerError() {

        VoteRequest voteRequest = VoteRequestFixture.getVoteRequest();
        Vote vote = VoteFixture.getFarovableVote();

        given(voteService.createVote(anyString(), any())).willReturn(
                Mono.error(new Exception("Internal Server Error.")));
        given(mapper.map(voteRequest, Vote.class)).willReturn(vote);

        webClient.post()
                .uri("/v1/vote/testeId")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(voteRequest), VoteRequest.class)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }
}