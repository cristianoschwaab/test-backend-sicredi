package br.com.sicredi.backendtest.service;

import br.com.sicredi.backendtest.entity.Discussion;
import br.com.sicredi.backendtest.repository.DiscussionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class DiscussionService {

    @Autowired
    private DiscussionRepository discussionRepository;

    public Mono<Discussion> createDiscussion(Discussion discussion) {
        discussion.setCreated(LocalDateTime.now());
        return discussionRepository.save(discussion);
    }

    public Mono<Discussion> findById(String id) {
        return discussionRepository.findById(id);
    }

}
