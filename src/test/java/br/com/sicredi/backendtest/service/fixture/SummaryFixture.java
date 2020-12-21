package br.com.sicredi.backendtest.service.fixture;

import br.com.sicredi.backendtest.entity.Discussion;
import br.com.sicredi.backendtest.entity.Summary;

public class SummaryFixture {

    public static Summary getSummary() {
        return Summary
                .builder()
                .id("Summary id")
                .discussionId("discussion id")
                .favorableVotes(1L)
                .favorablePercent(50D)
                .againstVotes(1L)
                .againstPercent(50D)
                .totalVotes(2L)
                .build();
    }
}
