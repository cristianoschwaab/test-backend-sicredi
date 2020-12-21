package br.com.sicredi.backendtest.service;

import br.com.sicredi.backendtest.entity.Discussion;
import br.com.sicredi.backendtest.repository.DiscussionRepository;
import br.com.sicredi.backendtest.service.fixture.DiscussionFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DiscussionServiceTest {

    @Mock
    private DiscussionRepository discussionRepository;

    @InjectMocks
    private DiscussionService discussionService;

    @Test
    public void shouldSuccessWhenCreateDiscussion() {

        Discussion discussion = DiscussionFixture.getDiscussion();

        given(discussionRepository.save(discussion)).willReturn(Mono.just(discussion));

        Mono<Discussion> discussionSaved = discussionService.createDiscussion(discussion);

        verify(discussionRepository, times(1)).save(discussion);

        StepVerifier
                .create(discussionSaved)
                .assertNext(value -> {
                    assertEquals(discussion.getId(), value.getId());
                    assertEquals(discussion.getTitle(), value.getTitle());
                    assertEquals(discussion.getDescription(), value.getDescription());
                    assertEquals(discussion.getCpfCreator(), value.getCpfCreator());
                    assertNotNull(value.getCreated());
                })
                .expectComplete()
                .verify();
    }

}