package br.com.sicredi.backendtest.controller.v1;

import br.com.sicredi.backendtest.controller.v1.fixture.SessionRequestFixture;
import br.com.sicredi.backendtest.controller.v1.model.SessionRequest;
import br.com.sicredi.backendtest.entity.Session;
import br.com.sicredi.backendtest.exception.ConflictException;
import br.com.sicredi.backendtest.exception.ExpectationException;
import br.com.sicredi.backendtest.service.SessionService;
import br.com.sicredi.backendtest.service.fixture.SessionFixture;
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
@WebFluxTest(SessionApi.class)
class SessionApiTest {

    @MockBean
    private SessionService sessionService;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private WebTestClient webClient;

    @Test
    void ShouldSuccessWhenCreated() {

        SessionRequest sessionRequest = SessionRequestFixture.getSessionRequest();
        Session session = SessionFixture.getSession();

        given(sessionService.createSession(anyString(), any())).willReturn(Mono.just(session));
        given(mapper.map(sessionRequest, Session.class)).willReturn(session);

        webClient.post()
                .uri("/v1/session/testeId")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(sessionRequest), SessionRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void shouldErrorWhenConflict() {

        SessionRequest sessionRequest = SessionRequestFixture.getSessionRequest();
        Session session = SessionFixture.getSession();

        given(sessionService.createSession(anyString(), any())).willReturn(
                Mono.error(new ConflictException("Conflict.")));
        given(mapper.map(sessionRequest, Session.class)).willReturn(session);

        webClient.post()
                .uri("/v1/session/testeId")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(sessionRequest), SessionRequest.class)
                .exchange()
                .expectStatus()
                .isEqualTo(409);
    }

    @Test
    void shouldErrorWhenExpectationFailed() {

        SessionRequest sessionRequest = SessionRequestFixture.getSessionRequest();
        Session session = SessionFixture.getSession();

        given(sessionService.createSession(anyString(), any())).willReturn(
                Mono.error(new ExpectationException("Expectation Failed.")));
        given(mapper.map(sessionRequest, Session.class)).willReturn(session);

        webClient.post()
                .uri("/v1/session/testeId")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(sessionRequest), SessionRequest.class)
                .exchange()
                .expectStatus()
                .isEqualTo(417);
    }

    @Test
    void shouldErrorWhenInternalServerError() {

        SessionRequest sessionRequest = SessionRequestFixture.getSessionRequest();
        Session session = SessionFixture.getSession();

        given(sessionService.createSession(anyString(), any())).willReturn(
                Mono.error(new Exception("Internal Server Error.")));
        given(mapper.map(sessionRequest, Session.class)).willReturn(session);

        webClient.post()
                .uri("/v1/session/testeId")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(sessionRequest), SessionRequest.class)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void ShouldSuccessWhenFoundDiscussion() {

        Session session = SessionFixture.getSession();

        given(sessionService.findOpenSessionByDiscussionId(anyString())).willReturn(Mono.just(session));

        webClient.get()
                .uri("/v1/session/testeId")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void ShouldErrorWhenNotFoundDiscussion() {

        given(sessionService.findOpenSessionByDiscussionId(anyString())).willReturn(Mono.empty());

        webClient.get()
                .uri("/v1/session/testeId")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

}