package br.com.sicredi.backendtest.service.fixture;

import br.com.sicredi.backendtest.entity.Vote;

public class VoteFixture {

    public static Vote getFarovableVote() {
        return Vote
                .builder()
                .id("Vote id")
                .discussionId("discussion id")
                .cpf("991.594.150-10")
                .vote(Boolean.TRUE)
                .build();
    }

    public static Vote getAgainstVote() {
        return Vote
                .builder()
                .id("Vote id")
                .discussionId("discussion id")
                .cpf("133.178.240-69")
                .vote(Boolean.FALSE)
                .build();
    }

}
