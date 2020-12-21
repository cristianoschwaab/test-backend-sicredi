package br.com.sicredi.backendtest.service.fixture;

import br.com.sicredi.backendtest.entity.Discussion;
import br.com.sicredi.backendtest.entity.Session;

import java.time.LocalDateTime;

public class SessionFixture {

    public static Session getSession() {
        Session session = new Session();
        session.setId("Session id");
        session.setDiscussionId("discussion id");
        session.setTimer(10L);
        session.setOpen(Boolean.TRUE);
        session.setCpfCreator("991.594.150-10");
        return session;
    }

    public static Session getCreatedSession() {
        Session session = new Session();
        session.setId("Session id");
        session.setDiscussionId("discussion id");
        session.setTimer(10L);
        session.setOpen(Boolean.TRUE);
        session.setCpfCreator("991.594.150-10");
        session.setCreated(LocalDateTime.now().minusHours(1));
        session.setClosed(LocalDateTime.now().plusHours(1));
        return session;
    }

    public static Session getExpiredession() {
        Session session = new Session();
        session.setId("Session id");
        session.setDiscussionId("discussion id");
        session.setTimer(10L);
        session.setOpen(Boolean.TRUE);
        session.setCpfCreator("991.594.150-10");
        session.setCreated(LocalDateTime.now().minusMinutes(1));
        session.setClosed(LocalDateTime.now());
        return session;
    }
}
