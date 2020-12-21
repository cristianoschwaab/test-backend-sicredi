package br.com.sicredi.backendtest.service;

import br.com.sicredi.backendtest.entity.Discussion;
import br.com.sicredi.backendtest.entity.Session;
import br.com.sicredi.backendtest.entity.Summary;
import br.com.sicredi.backendtest.integration.KafkaProducer;
import br.com.sicredi.backendtest.repository.SummaryRepository;
import br.com.sicredi.backendtest.service.fixture.DiscussionFixture;
import br.com.sicredi.backendtest.service.fixture.SessionFixture;
import br.com.sicredi.backendtest.service.fixture.SummaryFixture;
import br.com.sicredi.backendtest.service.fixture.VoteFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SummaryServiceTest {

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private VoteService voteService;

    @Mock
    private SummaryRepository summaryRepository;

    @InjectMocks
    private SummaryService summaryService;

    @Test
    public void shouldSuccessWhenCreateSummary() {

        Session session = SessionFixture.getSession();
        Summary summary = SummaryFixture.getSummary();

        given(summaryRepository.save(any())).willReturn(Mono.just(summary));
        doNothing().when(kafkaProducer).sendMessage(any());
        given(voteService.findByDiscussionId(session.getDiscussionId())).willReturn(
                Flux.just(VoteFixture.getFarovableVote(), VoteFixture.getAgainstVote()));

        Mono<Summary> summarySaved = summaryService.createSummary(session);

        verify(summaryRepository, times(1)).save(any());

        StepVerifier
                .create(summarySaved)
                .assertNext(value -> {
                    assertEquals(summary.getId(), value.getId());
                    assertEquals(summary.getDiscussionId(), value.getDiscussionId());
                    assertEquals(summary.getFavorableVotes(), value.getFavorableVotes());
                    assertEquals(summary.getFavorablePercent(), value.getFavorablePercent());
                    assertEquals(summary.getAgainstVotes(), value.getAgainstVotes());
                    assertEquals(summary.getAgainstPercent(), value.getAgainstPercent());
                    assertEquals(summary.getTotalVotes(), value.getTotalVotes());
                })
                .expectComplete()
                .verify();
    }

}