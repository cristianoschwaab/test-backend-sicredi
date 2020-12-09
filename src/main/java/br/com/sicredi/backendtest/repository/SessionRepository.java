package br.com.sicredi.backendtest.repository;

import br.com.sicredi.backendtest.entity.Session;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends ReactiveMongoRepository<Session, String> {

    Mono<Session> findByDiscussionId(String discussionId);

    Mono<Session> findByDiscussionIdAndOpen(String discussionId, Boolean open);

    Flux<Session> findByOpen(Boolean open);

}
