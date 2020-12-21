package br.com.sicredi.backendtest.controller.v1.fixture;

import br.com.sicredi.backendtest.controller.v1.model.DiscussionRequest;
import br.com.sicredi.backendtest.controller.v1.model.SessionRequest;

public class SessionRequestFixture {

    public static SessionRequest getSessionRequest() {
        SessionRequest request = new SessionRequest();
        request.setTimer(1L);
        request.setCpfCreator("991.594.150-10");
        return request;
    }
}
