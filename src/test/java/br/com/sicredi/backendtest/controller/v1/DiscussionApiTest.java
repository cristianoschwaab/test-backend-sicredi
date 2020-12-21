package br.com.sicredi.backendtest.controller.v1;

import br.com.sicredi.backendtest.controller.v1.fixture.DiscussionRequestFixture;
import br.com.sicredi.backendtest.controller.v1.model.DiscussionRequest;
import br.com.sicredi.backendtest.entity.Discussion;
import br.com.sicredi.backendtest.exception.ConflictException;
import br.com.sicredi.backendtest.exception.ExpectationException;
import br.com.sicredi.backendtest.service.DiscussionService;
import br.com.sicredi.backendtest.service.fixture.DiscussionFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@WebFluxTest(DiscussionApi.class)
class DiscussionApiTest {

    @MockBean
    private DiscussionService discussionService;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private WebTestClient webClient;

    @Test
    void ShouldSuccessWhenCreated() {

        DiscussionRequest discussionRequest = DiscussionRequestFixture.getDiscussionRequest();
        Discussion discussion = DiscussionFixture.getDiscussion();

        given(discussionService.createDiscussion(any())).willReturn(Mono.just(discussion));
        given(mapper.map(discussionRequest, Discussion.class)).willReturn(discussion);

        WebTestClient.ResponseSpec espec = webClient.post()
                .uri("/v1/discussion/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(discussionRequest), DiscussionRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void shouldErrorWhenConflict() {

        DiscussionRequest discussionRequest = DiscussionRequestFixture.getDiscussionRequest();
        Discussion discussion = DiscussionFixture.getDiscussion();

        given(discussionService.createDiscussion(any())).willReturn(
                Mono.error(new ConflictException("Conflict.")));
        given(mapper.map(discussionRequest, Discussion.class)).willReturn(discussion);

        WebTestClient.ResponseSpec espec = webClient.post()
                .uri("/v1/discussion/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(discussionRequest), DiscussionRequest.class)
                .exchange()
                .expectStatus()
                .isEqualTo(409);
    }

    @Test
    void shouldErrorWhenExpectationFailed() {

        DiscussionRequest discussionRequest = DiscussionRequestFixture.getDiscussionRequest();
        Discussion discussion = DiscussionFixture.getDiscussion();

        given(discussionService.createDiscussion(any())).willReturn(
                Mono.error(new ExpectationException("Expectation Failed.")));
        given(mapper.map(discussionRequest, Discussion.class)).willReturn(discussion);

        WebTestClient.ResponseSpec espec = webClient.post()
                .uri("/v1/discussion/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(discussionRequest), DiscussionRequest.class)
                .exchange()
                .expectStatus()
                .isEqualTo(417);
    }

    @Test
    void shouldErrorWhenInternalServerError() {

        DiscussionRequest discussionRequest = DiscussionRequestFixture.getDiscussionRequest();
        Discussion discussion = DiscussionFixture.getDiscussion();

        given(discussionService.createDiscussion(any())).willReturn(
                Mono.error(new Exception("Internal Server Error.")));
        given(mapper.map(discussionRequest, Discussion.class)).willReturn(discussion);

        WebTestClient.ResponseSpec espec = webClient.post()
                .uri("/v1/discussion/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(discussionRequest), DiscussionRequest.class)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

}