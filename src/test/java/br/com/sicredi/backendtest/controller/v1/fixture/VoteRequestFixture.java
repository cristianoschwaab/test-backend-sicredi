package br.com.sicredi.backendtest.controller.v1.fixture;

import br.com.sicredi.backendtest.controller.v1.model.SessionRequest;
import br.com.sicredi.backendtest.controller.v1.model.VoteRequest;

public class VoteRequestFixture {

    public static VoteRequest getVoteRequest() {
        VoteRequest request = new VoteRequest();
        request.setVote(Boolean.TRUE);
        request.setCpf("991.594.150-10");
        return request;
    }
}
