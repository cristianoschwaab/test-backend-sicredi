package br.com.sicredi.backendtest.repository;

import br.com.sicredi.backendtest.entity.Vote;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VoteRepository extends ReactiveMongoRepository<Vote, String> {

    Flux<Vote> findByDiscussionId(String discussionId);

    Mono<Vote> findByDiscussionIdAndCpf(String discussionId, String cpf);

}