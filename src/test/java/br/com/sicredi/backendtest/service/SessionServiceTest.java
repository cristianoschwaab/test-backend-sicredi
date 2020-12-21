package br.com.sicredi.backendtest.service;

import br.com.sicredi.backendtest.entity.Discussion;
import br.com.sicredi.backendtest.entity.Session;
import br.com.sicredi.backendtest.entity.Summary;
import br.com.sicredi.backendtest.exception.ConflictException;
import br.com.sicredi.backendtest.exception.ExpectationException;
import br.com.sicredi.backendtest.repository.SessionRepository;
import br.com.sicredi.backendtest.service.fixture.DiscussionFixture;
import br.com.sicredi.backendtest.service.fixture.SessionFixture;
import br.com.sicredi.backendtest.service.fixture.SummaryFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private DiscussionService discussionService;

    @Mock
    private SummaryService summaryService;

    @InjectMocks
    private SessionService sessionService;

    @Test
    public void shouldSuccessWhenCreateSession() {

        Session session = SessionFixture.getSession();
        Discussion discussion = DiscussionFixture.getDiscussion();

        given(sessionRepository.save(session)).willReturn(Mono.just(session));
        given(sessionRepository.findByDiscussionId(discussion.getId())).willReturn(Mono.empty());
        given(discussionService.findById(discussion.getId())).willReturn(Mono.just(discussion));

        Mono<Session> sessionSaved = sessionService.createSession(discussion.getId(), session);

        verify(sessionRepository, times(1)).save(session);

        StepVerifier
                .create(sessionSaved)
                .assertNext(value -> {
                    assertEquals(session.getId(), value.getId());
                    assertEquals(session.getDiscussionId(), value.getDiscussionId());
                    assertEquals(session.getTimer(), value.getTimer());
                    assertEquals(session.getOpen(), value.getOpen());
                    assertEquals(session.getCpfCreator(), value.getCpfCreator());
                    assertNotNull(value.getCreated());
                    assertNotNull(value.getClosed());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void shouldErrorWhenDiscussionNotFound() {

        Session session = SessionFixture.getSession();
        Discussion discussion = DiscussionFixture.getDiscussion();

        given(sessionRepository.save(session)).willReturn(Mono.just(session));
        given(discussionService.findById(discussion.getId())).willReturn(Mono.empty());

        Mono<Session> sessionSaved = sessionService.createSession(discussion.getId(), session);

        StepVerifier
                .create(sessionSaved)
                .expectErrorMatches(throwable ->
                        throwable instanceof ExpectationException
                                && throwable.getMessage().equals("A pauta de discussão informada não foi encontrada.")
                )
                .verify();
    }

    @Test
    public void shouldErrorWhenHasASession4Discussion() {

        Session session = SessionFixture.getSession();
        Discussion discussion = DiscussionFixture.getDiscussion();

        given(sessionRepository.save(session)).willReturn(Mono.just(session));
        given(sessionRepository.findByDiscussionId(discussion.getId())).willReturn(Mono.just(session));
        given(discussionService.findById(discussion.getId())).willReturn(Mono.just(discussion));

        Mono<Session> sessionSaved = sessionService.createSession(discussion.getId(), session);

        StepVerifier
                .create(sessionSaved)
                .expectErrorMatches(throwable ->
                        throwable instanceof ConflictException
                                && throwable.getMessage().equals("Já existe uma sessão criada para pauta de discussão.")
                )
                .verify();
    }

    @Test
    public void shouldSuccessWhenCloseSessions() {

        Session session = SessionFixture.getExpiredession();
        Summary summary = SummaryFixture.getSummary();

        given(sessionRepository.findByOpen(Boolean.TRUE)).willReturn(Flux.just(session));
        given(sessionRepository.save(session)).willReturn(Mono.just(session));
        given(summaryService.createSummary(any())).willReturn(Mono.empty());

        Flux<Session> sessionsClosed = sessionService.closeSessions();

        StepVerifier
                .create(sessionsClosed)
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @Test
    public void shouldSuccessWhenNoSessions2Close() {

        Session session = SessionFixture.getCreatedSession();

        given(sessionRepository.findByOpen(Boolean.TRUE)).willReturn(Flux.just(session));

        Flux<Session> sessionsClosed = sessionService.closeSessions();

        StepVerifier
                .create(sessionsClosed)
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

}