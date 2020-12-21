package br.com.sicredi.backendtest.service.fixture;

import br.com.sicredi.backendtest.entity.Discussion;

public class DiscussionFixture {

    public static Discussion getDiscussion() {
        Discussion discussion = new Discussion();
        discussion.setId("Discussion id");
        discussion.setTitle("Discussion Title");
        discussion.setDescription("Discussion Description");
        discussion.setCpfCreator("991.594.150-10");
        return discussion;
    }
}
