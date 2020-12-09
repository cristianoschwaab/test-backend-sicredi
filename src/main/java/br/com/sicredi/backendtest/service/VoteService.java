package br.com.sicredi.backendtest.service;

import br.com.sicredi.backendtest.entity.Session;
import br.com.sicredi.backendtest.entity.Vote;
import br.com.sicredi.backendtest.exception.ConflictException;
import br.com.sicredi.backendtest.exception.ExpectationException;
import br.com.sicredi.backendtest.exception.UnauthorizedException;
import br.com.sicredi.backendtest.integration.UsersClient;
import br.com.sicredi.backendtest.repository.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
public class VoteService {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UsersClient cpfIntegration;

    @Autowired
    private VoteRepository repository;

    public Mono<Vote> createVote(String discussionId, Vote vote) {

        vote.setDiscussionId(discussionId);
        vote.setCreated(LocalDateTime.now());

        return this.validateCredentials2Voting(vote.getCpf())
                .switchIfEmpty(this.validateRegisteredVote4Associate(discussionId, vote.getCpf()))
                .switchIfEmpty(repository.save(vote));
    }

    private Mono<Vote> validateCredentials2Voting(String cpf) {
        return cpfIntegration.findVoteCredentials(cpf).flatMap(usersResponse -> {
            if (usersResponse.getBody().isUnableToVote()) {
                log.error("Associate unable to Vote. CPF: {}", cpf);
                return Mono.error(new UnauthorizedException("Associado não possui credenciais para votação."));
            } else {
                return Mono.empty();
            }
        });
    }

    private Mono<Vote> validateRegisteredVote4Associate(String discussionId, String cpf) {

        Mono<Session> session = sessionService.findOpenSessionByDiscussionId(discussionId).switchIfEmpty(
                Mono.error(new ExpectationException("Não foi encontrada sessão aberta para a pauta de discussão informada.")));

        return session.flatMap(s -> repository.findByDiscussionIdAndCpf(s.getDiscussionId(), cpf)
                .flatMap(vote -> {
                    if (Objects.nonNull(vote)) {
                        log.error("Conflict vote already registered, vote: {}", vote);
                        return Mono.error(new ConflictException("Associado já possui voto registrado para a pauta de discussão."));
                    } else {
                        return Mono.empty();
                    }
                }));
    }

    public Flux<Vote> findByDiscussionId(String discussionId) {
        return repository.findByDiscussionId(discussionId);
    }
}
