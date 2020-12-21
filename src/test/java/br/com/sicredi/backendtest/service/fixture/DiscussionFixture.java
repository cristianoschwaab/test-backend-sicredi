package br.com.sicredi.backendtest.service.fixture;

import br.com.sicredi.backendtest.entity.Discussion;

public class DiscussionFixture {

    public static Discussion getDiscussion() {
        return Discussion
                .builder()
                .id("Discussion id")
                .title("Discussion Title")
                .description("Discussion Description")
                .cpfCreator("991.594.150-10")
                .build();
    }
}
