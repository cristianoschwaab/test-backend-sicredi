package br.com.sicredi.backendtest.service;

import br.com.sicredi.backendtest.entity.Session;
import br.com.sicredi.backendtest.entity.Summary;
import br.com.sicredi.backendtest.entity.Vote;
import br.com.sicredi.backendtest.integration.KafkaProducer;
import br.com.sicredi.backendtest.repository.SummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
public class SummaryService {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private VoteService voteService;

    @Autowired
    private SummaryRepository repository;

    public Mono<Summary> createSummary(Session session) {

        Summary summary = summaryDiscussionVotes(session.getDiscussionId());
        summary.setDiscussionId(session.getDiscussionId());
        summary.setCreated(LocalDateTime.now());
        Mono<Summary> saved = repository.save(summary);
        log.debug("Create summary votes {}", summary);

        kafkaProducer.sendMessage(saved.block());

        return saved;
    }

    private Summary summaryDiscussionVotes(String discussionId) {

        final Flux<Vote> votes = voteService.findByDiscussionId(discussionId);

        Summary summary = Summary.builder()
                .totalVotes(0L)
                .favorableVotes(0L)
                .favorablePercent(0D)
                .againstVotes(0L)
                .againstPercent(0D)
                .build();

        if (votes.hasElements().block()) {
            votes.count().subscribe(summary::setTotalVotes);
            // Favorables votes
            votes.filter(vote -> vote.getVote()).count().subscribe(count -> {
                summary.setFavorableVotes(count);
                summary.setFavorablePercent(count / votes.count().block().doubleValue() * 100);
            });
            // Againts votes
            votes.filter(vote -> !vote.getVote()).count().subscribe(count -> {
                summary.setAgainstVotes(count);
                summary.setAgainstPercent(count / votes.count().block().doubleValue() * 100);
            });
        }
        return summary;
    }
}
