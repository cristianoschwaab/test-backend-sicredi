package br.com.sicredi.backendtest.repository;

import br.com.sicredi.backendtest.entity.Discussion;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussionRepository extends ReactiveMongoRepository<Discussion, String> {

}
