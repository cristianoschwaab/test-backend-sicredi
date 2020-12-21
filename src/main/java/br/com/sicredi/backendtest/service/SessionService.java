package br.com.sicredi.backendtest.service;

import br.com.sicredi.backendtest.entity.Discussion;
import br.com.sicredi.backendtest.entity.Session;
import br.com.sicredi.backendtest.exception.ConflictException;
import br.com.sicredi.backendtest.exception.ExpectationException;
import br.com.sicredi.backendtest.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
public class SessionService {

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private SummaryService summaryService;

    @Autowired
    private SessionRepository repository;

    public Mono<Session> createSession(String discussionId, Session session) {
        session.setOpen(Boolean.TRUE);
        session.setDiscussionId(discussionId);
        session.setCreated(LocalDateTime.now());
        session.setTimer(Objects.nonNull(session.getTimer()) ? session.getTimer() : 1L);
        session.setClosed(session.getCreated().plusMinutes(session.getTimer()));

        return this.validateExistsSession4Discussion(discussionId)
                .switchIfEmpty(repository.save(session));
    }

    private Mono<Session> validateExistsSession4Discussion(String discussionId) {

        Mono<Discussion> discussion = discussionService.findById(discussionId).switchIfEmpty(
                Mono.error(new ExpectationException("A pauta de discussão informada não foi encontrada.")));

        return discussion.flatMap(d -> this.findSessionByDiscussionId(d.getId()).flatMap(session -> {
                    if (Objects.nonNull(session)) {
                        log.error("Conflict session exists for discussion. DiscussionId: {}, session: {}", discussionId, session);
                        return Mono.error(new ConflictException("Já existe uma sessão criada para pauta de discussão."));
                    } else {
                        return Mono.empty();
                    }
                }));
    }

    public Mono<Session> findSessionByDiscussionId(String discussionId) {
        return repository.findByDiscussionId(discussionId);
    }

    public Mono<Session> findOpenSessionByDiscussionId(String discussionId) {
        return repository.findByDiscussionIdAndOpen(discussionId, Boolean.TRUE);
    }

    @Scheduled(fixedDelay = 500)
    public Flux<Session> closeSessions() {
        return repository.findByOpen(Boolean.TRUE)
                .filter(session -> LocalDateTime.now().isAfter(session.getClosed()) || LocalDateTime.now().isEqual(session.getClosed()))
                .flatMap(this::closeSession);
    }

    public Mono<Session> closeSession(Session session) {
        session.setOpen(Boolean.FALSE);
        Mono<Session> closed = repository.save(session);
        log.debug("Close Session {}", session);
        summaryService.createSummary(closed.block());
        return closed;
    }

}
