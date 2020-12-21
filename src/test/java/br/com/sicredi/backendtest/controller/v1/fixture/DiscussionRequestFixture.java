package br.com.sicredi.backendtest.controller.v1.fixture;

import br.com.sicredi.backendtest.controller.v1.model.DiscussionRequest;

public class DiscussionRequestFixture {

    public static DiscussionRequest getDiscussionRequest() {
        DiscussionRequest request = new DiscussionRequest();
        request.setTitle("Discussion Title");
        request.setDescription("Discussion Description");
        request.setCpfCreator("991.594.150-10");
        return request;
    }
}
