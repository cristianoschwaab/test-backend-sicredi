package br.com.sicredi.backendtest.service.fixture;

import br.com.sicredi.backendtest.entity.Discussion;
import br.com.sicredi.backendtest.entity.Session;

import java.time.LocalDateTime;

public class SessionFixture {

    public static Session getSession() {
        return Session
                .builder()
                .id("Session id")
                .discussionId("discussion id")
                .timer(10L)
                .open(Boolean.TRUE)
                .cpfCreator("991.594.150-10")
                .build();
    }

    public static Session getCreatedSession() {
        return Session
                .builder()
                .id("Session id")
                .discussionId("discussion id")
                .timer(10L)
                .open(Boolean.TRUE)
                .cpfCreator("991.594.150-10")
                .created(LocalDateTime.now().minusHours(1))
                .closed(LocalDateTime.now().plusHours(1))
                .build();
    }

    public static Session getExpiredession() {
        return Session
                .builder()
                .id("Session id")
                .discussionId("discussion id")
                .timer(10L)
                .open(Boolean.TRUE)
                .cpfCreator("991.594.150-10")
                .created(LocalDateTime.now().minusMinutes(1))
                .closed(LocalDateTime.now())
                .build();
    }
}
