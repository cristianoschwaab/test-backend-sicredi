package br.com.sicredi.backendtest.repository;

import br.com.sicredi.backendtest.entity.Summary;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryRepository extends ReactiveMongoRepository<Summary, String> {

}
