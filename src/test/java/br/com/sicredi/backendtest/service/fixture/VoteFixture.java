package br.com.sicredi.backendtest.service.fixture;

import br.com.sicredi.backendtest.entity.Vote;

public class VoteFixture {

    public static Vote getFarovableVote() {
        Vote vote = new Vote();
        vote.setId("Vote id");
        vote.setDiscussionId("discussion id");
        vote.setCpf("991.594.150-10");
        vote.setVote(Boolean.TRUE);
        return vote;
    }

    public static Vote getAgainstVote() {
        Vote vote = new Vote();
        vote.setId("Vote id");
        vote.setDiscussionId("discussion id");
        vote.setCpf("133.178.240-69");
        vote.setVote(Boolean.FALSE);
        return vote;
    }

}
