package br.com.sicredi.backendtest.service;

import br.com.sicredi.backendtest.entity.Discussion;
import br.com.sicredi.backendtest.entity.Session;
import br.com.sicredi.backendtest.entity.Vote;
import br.com.sicredi.backendtest.exception.ConflictException;
import br.com.sicredi.backendtest.exception.ExpectationException;
import br.com.sicredi.backendtest.exception.UnauthorizedException;
import br.com.sicredi.backendtest.integration.UsersClient;
import br.com.sicredi.backendtest.integration.model.UsersResponse;
import br.com.sicredi.backendtest.repository.VoteRepository;
import br.com.sicredi.backendtest.service.fixture.DiscussionFixture;
import br.com.sicredi.backendtest.service.fixture.SessionFixture;
import br.com.sicredi.backendtest.service.fixture.VoteFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private UsersClient cpfIntegration;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private VoteService voteService;

    @Test
    public void shouldSuccessWhenRegisteredVote() {

        Vote vote = VoteFixture.getFarovableVote();
        Session session = SessionFixture.getSession();
        Discussion discussion = DiscussionFixture.getDiscussion();

        given(voteRepository.save(vote)).willReturn(Mono.just(vote));
        given(cpfIntegration.findVoteCredentials(vote.getCpf())).willReturn(
                Mono.just(ResponseEntity.ok(new UsersResponse(UsersResponse.CpfStatus.ABLE_TO_VOTE))));
        given(sessionService.findOpenSessionByDiscussionId(discussion.getId())).willReturn(Mono.just(session));
        given(voteRepository.findByDiscussionIdAndCpf(vote.getDiscussionId(), vote.getCpf())).willReturn(Mono.empty());

        Mono<Vote> voteSaved = voteService.createVote(discussion.getId(), vote);

        verify(voteRepository, times(1)).save(vote);

        StepVerifier
                .create(voteSaved)
                .assertNext(value -> {
                    assertEquals(vote.getId(), value.getId());
                    assertEquals(vote.getDiscussionId(), value.getDiscussionId());
                    assertEquals(vote.getCpf(), value.getCpf());
                    assertEquals(vote.getCpf(), value.getCpf());
                    assertNotNull(value.getCreated());
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void shouldErrorWhenUnableToVote() {

        Vote vote = VoteFixture.getFarovableVote();
        Session session = SessionFixture.getSession();
        Discussion discussion = DiscussionFixture.getDiscussion();

        given(voteRepository.save(vote)).willReturn(Mono.just(vote));
        given(cpfIntegration.findVoteCredentials(vote.getCpf())).willReturn(
                Mono.just(ResponseEntity.ok(new UsersResponse(UsersResponse.CpfStatus.UNABLE_TO_VOTE))));
        given(sessionService.findOpenSessionByDiscussionId(discussion.getId())).willReturn(Mono.just(session));

        Mono<Vote> voteSaved = voteService.createVote(discussion.getId(), vote);

        StepVerifier
                .create(voteSaved)
                .expectErrorMatches(throwable ->
                        throwable instanceof UnauthorizedException
                                && throwable.getMessage().equals("Associado não possui credenciais para votação.")
                )
                .verify();
    }

    @Test
    public void shouldErrorWhenNoOpenSessionByDiscussion() {

        Vote vote = VoteFixture.getFarovableVote();
        Discussion discussion = DiscussionFixture.getDiscussion();

        given(voteRepository.save(vote)).willReturn(Mono.just(vote));
        given(cpfIntegration.findVoteCredentials(vote.getCpf())).willReturn(
                Mono.just(ResponseEntity.ok(new UsersResponse(UsersResponse.CpfStatus.ABLE_TO_VOTE))));
        given(sessionService.findOpenSessionByDiscussionId(discussion.getId())).willReturn(Mono.empty());

        Mono<Vote> voteSaved = voteService.createVote(discussion.getId(), vote);

        StepVerifier
                .create(voteSaved)
                .expectErrorMatches(throwable ->
                        throwable instanceof ExpectationException
                                && throwable.getMessage().equals("Não foi encontrada sessão aberta para a pauta de discussão informada.")
                )
                .verify();
    }

    @Test
    public void shouldErrorWhenHasVote4DiscussionAndCpf() {

        Vote vote = VoteFixture.getFarovableVote();
        Session session = SessionFixture.getSession();
        Discussion discussion = DiscussionFixture.getDiscussion();

        given(voteRepository.save(vote)).willReturn(Mono.just(vote));
        given(cpfIntegration.findVoteCredentials(vote.getCpf())).willReturn(
                Mono.just(ResponseEntity.ok(new UsersResponse(UsersResponse.CpfStatus.ABLE_TO_VOTE))));
        given(sessionService.findOpenSessionByDiscussionId(discussion.getId())).willReturn(Mono.just(session));
        given(voteRepository.findByDiscussionIdAndCpf(vote.getDiscussionId(), vote.getCpf())).willReturn(Mono.just(vote));

        Mono<Vote> voteSaved = voteService.createVote(discussion.getId(), vote);

        StepVerifier
                .create(voteSaved)
                .expectErrorMatches(throwable ->
                        throwable instanceof ConflictException
                                && throwable.getMessage().equals("Associado já possui voto registrado para a pauta de discussão.")
                )
                .verify();
    }

    @Test
    public void shouldSuccessWhenFoundVotesByDiscussion() {

        given(voteRepository.findByDiscussionId("discussion id")).willReturn(
                Flux.just(VoteFixture.getFarovableVote(), VoteFixture.getAgainstVote()));

        Flux<Vote> votes = voteService.findByDiscussionId("discussion id");

        StepVerifier
                .create(votes)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    public void shouldSuccessWhenNotFoundVotesByDiscussion() {

        given(voteRepository.findByDiscussionId("discussion id")).willReturn(Flux.empty());

        Flux<Vote> votes = voteService.findByDiscussionId("discussion id");

        StepVerifier
                .create(votes)
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

}